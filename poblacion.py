k1 = 1          # Tasa de natalidad de los conejos
k2 = 0.01       # Capacidad de los zorros para cazar conejos
k3 = 0.5        # Mortalidad propia de los zorros
k4 = 0.05       # Tasa de natalidad de los zorros
delta_t = 0.001 # Incremento del tiempo que usaremos en nuestra simulación


# Escribe una función que toma el número de conejos y zorros actual y devuelve el incremento de conejos instantáneo
# es decir dR/dt
def delta_conejos(conejos, zorros):
    return ((k1 * conejos) - (k2 * conejos * zorros))


#Prueba para 100 conejos y 10 zorros:
resultado = delta_conejos(100, 10)
print(resultado)


# Escribe una función que toma el número de conejos y zorros actual y devuelve el incremento de zorros instantáneo
# es decir dF/dt
def delta_zorros(conejos, zorros):
    return ((k4 * conejos * zorros)-(k3 * zorros))