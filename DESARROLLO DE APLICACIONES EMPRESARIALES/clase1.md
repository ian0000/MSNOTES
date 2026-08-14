## Datos persistentes

- Los datos deben sobrevivir al software que los creo.
- En migraciones de datos los datos deben ser homologados y consistentes.
- Los datos deben ser consistentes y confiables.

## Concurrencia e interfaces

- cada tipo de usuario ve y opera los datos de diferente manera

## integraciones con tercero

- las aplicaciones no son islas todo sistema debe poder conversar aun que sean de distintas epocas o
  con diferentes pilas tecnologicas
- se debe replicar la informacion en distintos formatos

## logica de negocio

- la logica de negocion simepre va a cambiar con el timepo
- existen reglas y condiciones del negocio y los sistemas se deben adaptar a estas aun que cambien
  con el tiempo si no se vuelve en **deuda tecnica**

## **_no existen balas de plata_**

- distintos contextos necesitan distintas soluciones cada uno tiene que sacrificar algo para obtener
  algo

## complejidad esencial vs accidental

- esencial -> inherente al dominio negocio, a algo que gobierna el negocio, Req funcionales y no
  funcionales
- accidental -> algo que no esta en el dominio del negocio,

### ley de conway -> la arquitectura del software refleja la estructura de comunicacion del equipo, no la intencion del arquitecto

# economia del software

## el costo del cambio

el cambio es inevitable y el costo real del software es el mantenimiento no el desarrollo inicial.

- el software es una inversion de dinero
- cada decision y modificaion repercute en si se gana o no dinero
- no invertir demasiado en el diseño inicial cuando no se conocen todos los detalles
- YAGNI (**you arent gonna need it**) no se diseña por el por si acaso
