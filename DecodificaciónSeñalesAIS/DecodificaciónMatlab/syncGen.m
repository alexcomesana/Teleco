function syncIdeal = syncGen(samplesPerSymbol,sequence)
% Generate ideal AIS sync waveform

% Copyright 2016, The MathWorks, Inc.

% Set up training sequence

if nargin == 1
  tr1=logical([1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0]);
elseif nargin == 2
  tr1 = logical(sequence);
end

% Set up GMSK Modulator
mod=comm.GMSKModulator('BandwidthTimeProduct',0.3,'SamplesPerSymbol',samplesPerSymbol,'BitInput',true,'PulseLength',3,'SymbolPrehistory',[1 -1]);

% Apply NRZI encoding
for ii=2:length(tr1)
    if tr1(ii)==1
        tr1(ii)=tr1(ii-1);
    else
        tr1(ii)=~tr1(ii-1);
    end
end

% Generate GMSK waveform for training sequence
syncIdeal = step(mod,tr1');

