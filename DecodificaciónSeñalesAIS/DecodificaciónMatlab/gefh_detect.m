%
% GEFH_DETECT : esta rutina se utiliza para detectar entidades de corta 
%               duracion para cualquier radiogoniometro TSD-9XX
%
% Y = GEFH_DETECT(Flags_Deteccion,Flags_Postdeteccion)
%
% Flags_Deteccion : vector que contiene los flags de deteccion.
%
% Flags_Postdeteccion : vector que contiene los flags de postdeteccion.
%
% Y : estructura que contiene los bines inicial y final para cada entidad
%
%

function  Y = gefh_detect(Flags_Deteccion,Flags_Postdeteccion)

Y = [];
numero_hoppers = 0;
flag_hopper = 0;
  
for ciclo = 1:length(Flags_Deteccion)
    
  C1 = (Flags_Deteccion(ciclo) == 1) & (flag_hopper == 0);
  C2 = (Flags_Postdeteccion(ciclo) == 0) & (flag_hopper == 1);
    
  if (C1 == 1)
    Y(numero_hoppers+1).Bin_Inicial = ciclo;
    flag_hopper = 1;
  end % if
    
  if (C2 == 1)
    Y(numero_hoppers+1).Bin_Final = ciclo-1;	  	
    numero_hoppers = numero_hoppers + 1;
    flag_hopper = 0;
  end % if  
end %for
  
% Ultimo hopper

if (flag_hopper == 1)
  Y(numero_hoppers+1).Bin_Final = ciclo;	
  numero_hoppers = numero_hoppers + 1;
end % if