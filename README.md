# Trabajo Práctico Integrador-Sistema Operativo

El objetivo de esta práctica consiste en la implementación de un simulador que permita mostrar los aspectos de la Planificación de Procesos a Corto Plazo y la gestión de la memoria con particiones Fijas dentro de un esquema de un solo procesador, tratando el ciclo de vida completo de un proceso desde su ingreso al sistema hasta su finalización.

### Requisitos establecidos:

* Se permitirán como máximo 10 procesos .

* El esquema de particiones será el siguiente:
  100K destinados al Sistema Operativo
  250K para trabajos más grandes.
  120K para trabajos medianos .
  60K para trabajos pequeños.

* Política de asignación de memoria: Best-Fit

* Planificación de CPU: algoritmo SRTF (SJF preemptible).

### Criterios de expropiación: 

1. Si un proceso está en ejecución y se admite en la cola de listos, un proceso nuevo con mayor prioridad de ejecución,  entonces se saca al proceso actual de CPU, sin suspenderlo, y se le asigna la CPU al proceso con mayor prioridad.
2. En caso que el proceso con mayor prioridad haya ingresado a la cola de procesos listos y suspendidos, entonces podrá suspenderse un proceso ya asignado a memoria (utilizando swapout) para darle lugar en memoria al proceso con mayor prioridad. Para sacar un proceso de memoria, el criterio será el siguiente:
a- Primero se intentará suspender un proceso que no esté en CPU.
b- Si el proceso nuevo no cabe en ninguno de los bloques de los procesos que no están en CPU, y cabe en la partición del que está en la CPU, entonces se suspenderá al proceso que está CPU, para colocar el nuevo proceso (que tiene mayor prioridad) en su lugar de memoria.
c- Luego se le asignará la CPU al nuevo proceso de mayor prioridad, que ahora sí se encuentra en estado de Listo.
Luego se continúa con la ejecución del proceso nuevo, con mayor prioridad.
Deberán definir ademas el funcionamiento y controles del planificador a mediano plazo, que es el que decide cuándo y mediante qué controles el proceso suspendido vuelve a la memoria (y, por ende, a la cola de Listos)

### Pre-requisitos 📋

Se necesita descargar el archivo "input.txt" para la carga de los procesos.

### Ejecución 🔧

Ingresar por consola el comando **java -jar tpi.jar**

![ejecutar](https://user-images.githubusercontent.com/21130494/143786514-0a6d01a6-d5b4-404e-9911-79916dd9f2c4.png)

Se abrirá una ventana emergente solicitando que seleccione el archivo de entrada.Seleccionar **input.txt**

![seleccionar archivo](https://user-images.githubusercontent.com/21130494/143786605-90505153-ad8c-460e-875d-6899f2adb957.png)

Una vez seleccionado se mostrará los resultados del programa.

![Programa](https://user-images.githubusercontent.com/21130494/143786616-1c39c774-47bb-45d7-a515-157240f8fddb.png)

## Ejemplo de pruebas ⚙️

Tabla de procesos y su correspondiente diagrama de gantt aplicando ** algoritmo SRTF**

![Captura de pantalla de 2021-11-28 18-27-54](https://user-images.githubusercontent.com/21130494/143786692-8a323143-56f2-4a51-8fdd-90bf31447b3a.png)

Estado de las particiones de memorias, las colas listo, Listo y suspendido y la cola saliente en cada instantes de tiempo y el proceso que se encuentra ejecutándose (CPU)

![Captura de pantalla de 2021-11-28 18-28-06](https://user-images.githubusercontent.com/21130494/143786821-3f74033d-f368-4909-aed3-01971d185240.png)

En el mismo directorio donde se encuentra el archivo de entrada guardará un archivo de salida con los resultados del programa.
![Captura de pantalla de 2021-11-28 18-37-03](https://user-images.githubusercontent.com/21130494/143786982-26767a11-188f-4a09-8ed7-5bbb2114c50d.png)

#  Link del video:
https://youtu.be/eBXCYRjL56s

## Autores ✒️

Grupo 1- Turno Tarde UTN FRRe

* **Monzón Leticia.**
* **Ojeda Guido Thomas** 
* **Ojeda Núñez, Pablo Nicolás**
* **Orgoñ Nahiara** 

---
