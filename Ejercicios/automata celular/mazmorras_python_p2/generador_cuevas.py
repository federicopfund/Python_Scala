"""
	GENERADOR DE CUEVAS EN PYGAME
	Genera mazmorras en forma de cueva utilizando un automata celular y dibuja el mapa
	en una ventana de Pygame

	Escrito por Glare y Transductor
	www.robologs.net
"""


import random
import numpy as np
import sys
import pygame

def main():


	#Pediremos al usuario que entre a mano los parametros:
	print("\n--Introduce los parametros del generador--\n")

	#Dimensiones del mapa (por lo menos tiene que ser 20)
	dimx = max(int(input("Ancho del mapa: ")), 20)
	dimy = max(int(input("Alto del mapa: ")), 20)

	#Minimo de vecinos que debe tener una celda para sobrevivir
	lim_aislamiento = int(input("Limite para aislamiento (recomendado 3): "))

	#Minimo de vecinos para que una celda muerta pase a estar viva
	lim_nacimiento = int(input("Limite para nacimiento (recomendado 4): "))

	#Numero de iteraciones para el automata celular
	n = int(input("Num. de pasos para el automata celular (recomendado 5-6): "))

	#Probabilidad de inicializar una celda como viva
	P = float(input("Probabilidad inicializar una casilla como viva (recomendado 0.4): "))


 
	# El array 'mapa' guarda el contenido de cada casilla de la mazmorra.
	# Lo inicializamos con todas las celdas = 0
	mapa = np.zeros((dimy,dimx), dtype=int)

	#Inicializar el mapa aleatoriamente
	inicializarMapa(mapa, P)

	#Hacer 'n' pasos del automata celular
	for i in range(n):
		mapa = calcularPaso(mapa, lim_aislamiento, lim_nacimiento)

	#Eliminar las zonas aisladas
	mapa = eliminarZonasAisladas(mapa)

	print("\n\n------------------------------------------\n\n")

	#-----PYGAME------

	#Dimensiones (en px) del dibujo de cada casilla
	casillax = 16
	casillay = 16

	#Dimensiones de la ventana de pygame donde se dibuja el mapa
	ancho_ventana = dimx*16
	alto_ventana = dimy*16

	#Crear la ventana y cambiar su nombre
	gameDisplay = pygame.display.set_mode((ancho_ventana, alto_ventana))
	pygame.display.set_caption('Cave Party')

	
	#Dibujar la ventana con el mapa
	gameDisplay.fill((0,0,0))
	dibujarMapa(mapa, gameDisplay, casillax, casillay)


	#Esperar a que el jugador se canse de mirar el mapa...
	print("\nCierra la ventana de Pygame para detener el programa")
	continuar = True
	while continuar:
		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				continuar = False


def inicializarMapa(mapa, P):
	# Esta funcion inicializa el mapa. Cada casilla tiene una probabilidad P de
	# inicializarse como viva.

	#Recorrer el mapa e inicializar todas las casillas:
	for i in range(len(mapa)):
		for j in range(len(mapa[0])):

			#Generar un valor aleatorio entre 0 y 1
			valor = random.uniform(0, 1)

			#Si este valor es inferior a la probabilidad, crear una casilla viva:
			if valor < P:
				mapa[i][j] = 1

			#(De lo contrario, la casilla permanece muerta)

def contarVecinosVivos(mapa, x, y):
	# Esta funcion cuenta el numero de vecinos vivos que tiene 
	# la casilla situada en mapa[y][x]

	#Guardar las dimensiones del mapa
	dimy = len(mapa)
	dimx = len(mapa[0])
	
	#Contador para el numero de vecinos vivos:
	contador = 0
	
	#Doble bucle para comprobar los vecinos
	for i in range(-1, 2):
		for j in range(-1, 2):
			
			#No queremos contar la casilla del centro!
			if (i == 0 and j == 0):
				pass

			#Si no estamos en el centro...
			else:
				#Guardar las coordenadas del vecino
				vecinox = x + i
				vecinoy = y + j

				#Supondremos que todas las casillas que hay fuera del mapa estan 'vivas' (son paredes)
				if (vecinox < 0 or vecinox >= dimx or vecinoy < 0 or vecinoy >= dimy):
					contador += 1

				# Si es una casilla del interior del mapa se suma su valor al contador
				# (0 si esta muerta y 1 si esta viva)
				else:
					contador += mapa[vecinoy][vecinox]

	#Devolver el numero de vecinos
	return contador

def calcularPaso(mapa, lim_aislamiento, lim_nacimiento):
	#Esta funcion calcula una iteracion del automata celular

	#Guardar las dimensiones del mapa original
	dimy = len(mapa)
	dimx = len(mapa[0])

	#Crear un mapa auxiliar
	mapa_aux = np.zeros((dimy, dimx), dtype = int)
	#El valor de las casillas se actualizará en el mapa auxiliar

	#Recorrer cada casilla del mapa y aplicar las reglas del automata cel.
	for x in range(dimx):
		for y in range(dimy):

			#Calcular el numero de vecinos
			num_vecinos = contarVecinosVivos(mapa, x, y)
			
			#Si la celda esta viva, comprobar si muere de aislamiento
			if mapa[y][x] == 1:

				if num_vecinos < lim_aislamiento:
					mapa_aux[y][x] = 0

				else:
					mapa_aux[y][x] = 1

			#Si la celda esta muerta, comprobar si tiene que nacer
			elif mapa[y][x] == 0:

				if num_vecinos > lim_nacimiento:
					mapa_aux[y][x] = 1
				else:
					mapa_aux[y][x] = 0

	#Actualizar el mapa original
	return mapa_aux



def dibujarMapa(mapa, gameDisplay, casillax, casillay):
	# Esta funcion dibuja el mapa con caracteres en la terminal y
	# con imagenes en la ventana gameDisplay

	#Guardar las dimensiones del mapa original
	dimy = len(mapa)
	dimx = len(mapa[0])
 
	#Borrar la ventana
	gameDisplay.fill((25,25,25))

	#Dibujar las casillas del mapa
	for y in range(dimy):
		for x in range(dimx):
			if mapa[y][x] == 0:
				sys.stdout.write("  ") #Espacio vacio
				tile = pygame.image.load('mazmorra_pygame_p2_tiles/floor.bmp').convert_alpha()
				gameDisplay.blit(tile, (x*casillax,y*casillay))

			elif mapa[y][x] == 1:
				sys.stdout.write("# ") #Muro

				#Si en la casilla de abajo hay una pared, habra que colocar un muro de tipo 2
				if (y+1) >= dimy or mapa[y+1][x] == 1:
					tile = pygame.image.load('mazmorra_pygame_p2_tiles/wall2.bmp').convert_alpha()

				#De lo contrario, un muro de tipo 1
				else:
					tile = pygame.image.load('mazmorra_pygame_p2_tiles/wall1.bmp').convert_alpha()
					gameDisplay.blit(tile, (x*casillax,y*casillay))
	         
				gameDisplay.blit(tile, (x*casillax,y*casillay))
 
				# Para dar el efecto de 2.5D, las paredes van a tapar una parte de la casilla que tienen
				# detras (siempre que no sea otro muro)
				if y-1 >= 0:
					if mapa[y-1][x] != 1:
						image = pygame.image.load('mazmorra_pygame_p2_tiles/wall_border_alpha.bmp')
		                
						#Pintar objetos con alpha es mas complicado en pygame...
						surface = pygame.Surface(image.get_size())
						key = (255,0,255)
						surface.fill(key, surface.get_rect())
						surface.set_colorkey(key)
						surface.blit(image, (0,0))
						surface.set_alpha(255)

						gameDisplay.blit(surface, (x*casillax,(y-1)*casillay))
    
		print("")
     
	print("\n\n")

	#Update!
	pygame.display.update()

def eliminarZonasAisladas(mapa):
	# Esta funcion separa cada region del mapa y las elimina todas excepto
	# la que tenga mayor area

	#Guardar las dimensiones del mapa original
	dimy = len(mapa)
	dimx = len(mapa[0])

	#La primera region tendra la etiqueta '2'
	region = 2

	#Vector para guardar el valor de las areas de cada region
	areas = []
	#NOTA: el elemento [0] se corresponde a la region '2', [1] es la region '3', etc.

	#Crear un mapa auxiliar que sea una copia del mapa original
	mapa_aux = np.copy(mapa)	

	#Recorremos cada casilla del mapa
	for x in range(dimx):
		for y in range(dimy):

			#Si la casilla actual es un espacio vacio, aplicar FloodFill para "pintar" toda la region
			if mapa_aux[y][x] == 0:

				#Aplicar Flood-Fill y guardar el area en el vector
				areas.append(floodFill(mapa_aux, y, x, region))

				#Sumar +1 al contador de regiones
				region = region + 1

	#Nos quedamos con la region que tenga area maxima
	region_maxima = areas.index(max(areas)) + 2

	#En el mapa original, eliminamos todas las casillas que no esten en region_maxima
	for x in range(dimx):
		for y in range(dimy):
			if mapa_aux[y][x] != 1 and mapa_aux[y][x] != region_maxima:
				mapa[y][x] = 1

	#Devolver el mapa actualizado
	return mapa


def floodFill(mapa, posy, posx, num_region):
	# Llena todas las casillas de una region con el mismo numero (num_region)
	# y devuelve el area de la region

	#Guardar las dimensiones del mapa original
	ysize = len(mapa)
	xsize = len(mapa[0])

	#Crear una estructura de pila ('stack'), donde guardaremos las casillas de la region
	pila = set(((posy, posx),))

	#Variable para guardar el area de la region
	area = 0

	#Repetir mientras haya elementos en la pila
	while len(pila) > 0:

		#Guardar las coordenadas (fila, columna) del objeto de la pila
		y, x = pila.pop()

		#Sumar +1 al area de la region
		area = area + 1

		#"Pintar" la casilla
		if mapa[y][x] == 0:
			mapa[y][x] = num_region
			if y > 0:
				pila.add((y - 1, x))
			if y < (ysize - 1):
				pila.add((y + 1, x))
			if x > 0:
				pila.add((y, x - 1))
			if x < (xsize - 1):
				pila.add((y, x + 1))

	#Devolver el area total de la region
	return area


#Llamar funcion main al iniciar el programa
main()
