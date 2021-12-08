#!/usr/bin/env python
# coding: utf-8


from matplotlib import pyplot as plt


# Vamos a crear una pequeña simulación de la evolución de una población de conejos y zorros haciéndo uso de un modelo matemático simple:
# 
#     dR/dt = k1 * R - k2 * R * F  <- Es la variación instantanea en el número de conejos R
#     dF/dt = k4 * R * F - k3 * F  <- Es la variación instantanea en el número de zorros F
#    


k1 = 1          # Tasa de natalidad de los conejos
k2 = 0.01       # Capacidad de los zorros para cazar conejos
k3 = 0.5        # Mortalidad propia de los zorros
k4 = 0.05       # Tasa de natalidad de los zorros
delta_t = 0.001 # Incremento del tiempo que usaremos en nuestra simulación
CONEJOS_INICIALES = 1000        # Fijemos estos valores en unas variables globales
ZORROS_INICIALES = 10


# Escribe una función que toma el número de conejos y zorros actual y devuelve el incremento de conejos instantáneo
# es decir dR/dt
def delta_conejos(conejos, zorros):
    return ((k1 * conejos) - (k2 * conejos * zorros))


# Escribe una función que toma el número de conejos y zorros actual y devuelve el incremento de zorros instantáneo
# es decir dF/dt
def delta_zorros(conejos, zorros):
    return ((k4 * conejos * zorros)-(k3 * zorros))


# Escribe una función que toma el número de conejos y zorros actual y devulve una tupla con los valores 
# de dR/dt y dF/dt
def delta_poblacion(conejos, zorros):
    num_conejos = delta_conejos(conejos, zorros)
    num_zorros = delta_zorros(conejos, zorros)
    poblacion = (num_conejos, num_zorros)
    return poblacion


# Escribe una función que dado el número de conejos y de zorros inicial y un número de peridos nos 
# devuelva una lista con el resultado de avanzar la población ese número de periodos:
#
# simular(CONEJOS_INICIALES, ZORROS_INICIALES, 10) devolverá algo del estilo de:
#
#   [(1000, 10), (980.254885, 11.2257), (970.5468, 12.2258)...] 
#
# La lista contiene tantos elementos como periodos:
#
def simular(conejos, zorros, periodos):
    
    #Inicio de proceso y doy valores iniciales
    lista_poblacion = [(conejos, zorros)]
    conejos_anteriores = conejos
    zorros_anteriores = zorros
    
    #Operacion por periodo
    for n in range(1, periodos):
        
        #Cojo los incrementos
        dRdt, dFdt = delta_poblacion(conejos_anteriores, zorros_anteriores)
        
        #Calculo los nuevos valores
        conejos_nuevos = conejos_anteriores + dRdt *delta_t
        zorros_nuevos = zorros_anteriores + dFdt * delta_t 
        
        #Guardo los nuevos valores en lista (como tupla)
        lista_poblacion.append((conejos_nuevos, zorros_nuevos))
        
        #Paso los valores calculados como nuevos valores anteriores
        conejos_anteriores = conejos_nuevos
        zorros_anteriores = zorros_nuevos
        
    return lista_poblacion


# Evaluemos 1.000.000 de periodos y guardemos el resultado en la variable global HISTORIA
HISTORIA = simular(CONEJOS_INICIALES, ZORROS_INICIALES, 1000000)


# Extraigamos la historia de los conejos y la de los zorros a las variables globales correspondientes
HISTORIA_CONEJOS = list(map(lambda punto: punto[0], HISTORIA))
HISTORIA_ZORROS = list(map(lambda punto: punto[1], HISTORIA))


# Generemos una gráfica de dichos valores x = conejos, y = zorros
plt.plot(HISTORIA_CONEJOS, HISTORIA_ZORROS)
plt.title('Evolucion de la población de conejos vs zorros')
plt.xlabel('Num. Conejos')
plt.ylabel('Num. Zorros')
plt.show()

# Generemos una gráfica de la evolución de los conejos
plt.plot(HISTORIA_CONEJOS)
plt.title('Evolucion de la población de conejos')
plt.xlabel('Tiempo')
plt.ylabel('Num. Conejos')
plt.show()