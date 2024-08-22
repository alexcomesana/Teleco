%
%
%
function [validCRC,Message] = ais_burst_demod(Samples,Fs,SamplesPerSymbol)
validCRC = 0;
Message = [];

% Preamble for correlation purposes
syncCalc = syncGen(SamplesPerSymbol);
syncIdeal = angle(syncCalc);

% Checksum using comm.CRCGenerator
crcGen = comm.CRCGenerator('Polynomial','X^16 + X^12 + X^5 + 1',...
                           'InitialConditions',1,'DirectMethod',true,...
                           'FinalXOR',1);

% Gaussian Filter Design
BT=.3;
pulseLength=2;
gx = gaussdesign(BT,pulseLength,SamplesPerSymbol);

% Gaussian filtering for ISI cancelation and phase calculation
rxf = filter(gx,1,Samples);
rxAngles = unwrap(angle(rxf));

% x0 = (1:5:length(rxAngles));
% y0 = rxAngles(x0);
% xx = 1:length(rxAngles);
% yy0 = spline(x0,y0,xx);
% 
% x1 = (2:5:length(rxAngles));
% y1 = rxAngles(x1);
% yy1 = spline(x1,y1,xx);
% 
% x2 = (3:5:length(rxAngles));
% y2 = rxAngles(x2);
% yy2 = spline(x2,y2,xx);
% 
% x3 = (4:5:length(rxAngles));
% y3 = rxAngles(x3);
% yy3 = spline(x3,y3,xx);
% 
% x4 = (5:5:length(rxAngles));
% y4 = rxAngles(x4);
% yy4 = spline(x4,y4,xx);
% 
% rxAngles_ = rxAngles;
% rxAngles = transpose((yy0+yy1+yy2+yy3+yy4)/5);


% Fine frequency correction: consider the last 5 minima and the last five
% maxima that come before the last minimum
test = rxAngles(1:160);                                                                  
maxima = find((test(1:end-2) <= test(2:end-1))&(test(2:end-1) >= test(3:end)))+1;
minima = find((test(1:end-2) >= test(2:end-1))&(test(2:end-1) <= test(3:end)))+1;
        
lastMinimum = max(minima);
maxima = maxima(find(maxima < lastMinimum));

% Compute the phase correction
numMaxima = min(5,length(maxima));
xx = maxima(length(maxima)-numMaxima+1:length(maxima));
[q,Sq] = polyfit(xx,rxAngles(xx),1);

numMinima = min(5,length(minima));
xx = minima(length(minima)-numMinima+1:length(minima));
[p,Sp] = polyfit(xx,rxAngles(xx),1);



% Phase correction done
xx = (1:length(rxAngles))';
rxAngles = mod(rxAngles-polyval((p+q)/2,xx)+pi,2*pi)-pi;
rxAngles = unwrap(rxAngles);

% Find the preamble location via correlation. 
syncCorr   = zeros(SamplesPerSymbol*50,1);
syncVector = zeros(length(syncIdeal),1);
   
if (length(rxAngles) > SamplesPerSymbol*50 + length(syncIdeal))
  for ii=1:SamplesPerSymbol*50
    syncVector   = rxAngles(ii:ii+length(syncCalc)-1);  
    syncCorr(ii) = (mean(syncIdeal.*syncVector)-...
                    mean(syncIdeal)*mean(syncVector))/...
                    (std(syncIdeal,1)*std(syncVector,1));
  end
else
  syncCorr(1)=1;
end

% Compute best sample phase for making bit decisions. If there are
% more than 1 peaks in the correlation sequence whose levels are 
% pretty much the same as the highest one, the latest one will be
% taken.  This is done so because of the fact that there are some
% ramp up bits that can mislead us when it comes to finding the
% position of the preamble.  It is assumed that the last highest
% peak is associated with the real preamble.
maxima = find((syncCorr(1:end-2) <= syncCorr(2:end-1)) & ...
              (syncCorr(2:end-1) >= syncCorr(3:end)))+1;

idx = maxima(max(find(abs(max(syncCorr)-syncCorr(maxima)) < 0.1*max(syncCorr))));
m = syncCorr(idx);

if m < 0.1
  return;
end %if

% Codigo en el que se se calculan los bits usando la fase corregida
% de la que se ha quitado el termino lineal en frecuencia
samplePhase = idx + floor(SamplesPerSymbol/2)+2;
abits = zeros(size(rxAngles(samplePhase:SamplesPerSymbol:end)));
ind   = find(abs(diff(rxAngles(samplePhase:SamplesPerSymbol:end))) > pi/4);       
abits(ind) = 1;

% Search the first 50 bits for the StartByte flag 0x7E
sb=1;
if length(abits) > 50
  for ii=2:50
    if (sum(abits(ii:ii+5))==6 && abits(ii-1)==0 && abits(ii+6)==0 && sb==1)
      sb=ii+7;
    end
  end
end

% Read the message type and route the bits to the correct decode
% function
msgType=0;
if length(abits) >= sb+7
  msgType = transpose(abits((2:7)+sb))*2.^(0:5)';
end

% Follow AIS spec to unstuff adata_outfter 5 consecutive 1's.  This will 
% unstuff everything, including the end flag in the AIS message. 
ubits=aisUnstuff(abits(sb:end));

% Decode message based on detected message type
% Compute the checksum, compare to the received message bits, and
% if the checksum passes flip the bytes and decode the message.

switch msgType
case 1
  if length(ubits)>=184
    checkSum = step(crcGen,ubits(1:168));
    if isequal(checkSum(169:184),ubits(169:184))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 2
  if length(ubits)>=184
    checkSum = step(crcGen,ubits(1:168));
    if isequal(checkSum(169:184),ubits(169:184))
      validCRC=1;
    else
      validCRC=0;
    end
      reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 3
  if length(ubits)>=184
    checkSum = step(crcGen,ubits(1:168));
    if isequal(checkSum(169:184),ubits(169:184))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 4
  if length(ubits)>=184
    checkSum = step(crcGen,ubits(1:168));
    if isequal(checkSum(169:184),ubits(169:184))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else 
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 5
  if length(ubits)>=440
    checkSum = step(crcGen,ubits(1:424));
    if isequal(checkSum(425:440),ubits(425:440))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 18
  if length(ubits)>=184
    checkSum = step(crcGen,ubits(1:168));
    if isequal(checkSum(169:184),ubits(169:184))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 19
  if length(ubits)>=328
    checkSum = step(crcGen,ubits(1:312));
    if isequal(checkSum(313:328),ubits(313:328))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
  
case 21
  if length(ubits)>=288
    checkSum = step(crcGen,ubits(1:272));
    if isequal(checkSum(273:288),ubits(273:288))
      validCRC=1;
    else
      validCRC=0;
    end
    reset(crcGen);
  else
    validCRC=0;
    fprintf('msg type %d was cut. No CRC calculated\n',int32(msgType));
  end
otherwise
  disp('Message Checksum Failed');
  validCRC=0;
end %switch

if validCRC==1
  Message = zeros(4*ceil(length(ubits)/4),1);
  Message(1:length(ubits)) = ubits;
  binary = reshape(Message,4,length(Message)/4);
  hex = binary'*[8;4;2;1];
  fprintf('msg type %d CRC %d: %s\n',int32(msgType),validCRC,sprintf('%s',dec2hex(hex,1)));   
else
  fprintf('Message Checksum Failed\n');   
end



