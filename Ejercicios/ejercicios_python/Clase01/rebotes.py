#La pelota que rebota.
altura = 100
rebote = 0
while rebote <= 11:
    rebote += 1  
    altura = altura *(3/5) 
    print(rebote,round(altura,2))


