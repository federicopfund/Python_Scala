# hipoteca.py
# Archivo de ejemplo
# Ejercicio de hipoteca

""""David solicitó un crédito a 30 años para comprar una vivienda, 
    con una tasa fija nominal anual del 5%.
    Pidió $500000 al banco y acordó un pago mensual fijo de $2684"""
    
saldo = 500000.0
tasa = 0.05
pago_mensual = 2684.11
total_pagado = 0.0

while saldo > 0:
    saldo = saldo * (1+tasa/12) - pago_mensual
    total_pagado = total_pagado + pago_mensual

print('Total pagado', round(total_pagado, 2))

""""Ejercicio 1.8: Adelantos
    Supongamos que David adelanta pagos extra de $1000/mes,
    durante los primeros 12 meses de la hipoteca."""
"""
adelanta=1000    
saldo = 500000.0
tasa = 0.05
pago_mensual = 2684.11
total_pagado = 0.0
mes_actual=0
while saldo > 0:
    pago_mensual_ef = pago_mensual
    mes_actual = mes_actual + 1
    if mes_actual <=12:
        pago_mensual_ef=pago_mensual+adelanta
    saldo = saldo * (1+tasa/12) - pago_mensual_ef
    total_pagado = total_pagado + pago_mensual_ef 
    print("Meses:",mes_actual,"\ntotal Pagado:",round(total_pagado,2))      
    #print(f'mes:{mes_actual}\nResto a pagar:{saldo}\nTotal pagado:{round(total_pagado, 2)}')"""
    
"""Ejercicio 1.9: Calculadora de adelantos"""
"""¿Cuánto pagaría David si agrega $1000 por mes durante cuatro años, 
    comenzando en el sexto año de la hipoteca (es decir, luego de 5 años)?"""
    
"""
adelanta=1000    
saldo = 500000.0
tasa = 0.05
pago_mensual = 2684.11
total_pagado = 0.0
mes_actual=0
pago_extra_mes_comienzo=60
pago_extra_mes_fin = 108
while saldo > 0:
    pago_mensual_ef = pago_mensual
    mes_actual = mes_actual + 1
    if pago_extra_mes_comienzo <mes_actual <=pago_extra_mes_fin:
        pago_mensual_ef=pago_mensual+adelanta
    saldo = saldo * (1+tasa/12) - pago_mensual_ef
    total_pagado = total_pagado + pago_mensual_ef 
    print(f'{mes_actual}:{round(total_pagado,2)}   {round(saldo,2)}')     
print(f'Mes:{mes_actual}\nResto a pagar:{round(saldo,2)}\nTotal pagado:{round(total_pagado, 2)}')"""


"""Ejercicio 1.11: Bonus"""
adelanta=1000    
saldo = 500000.0
tasa = 0.05
pago_mensual = 2684.11
total_pagado = 0.0
mes_actual=0
pago_extra_mes_comienzo=60
pago_extra_mes_fin = 108
while saldo > 0:
    pago_mensual_ef = pago_mensual
    mes_actual = mes_actual + 1
    if pago_extra_mes_comienzo <mes_actual <=pago_extra_mes_fin:
        pago_mensual_ef=pago_mensual+adelanta
    if pago_mensual_ef>saldo:
        pago_mensual_ef=saldo*(1 + tasa/12)
        
    saldo = saldo * (1 + tasa/12) - pago_mensual_ef
    total_pagado = total_pagado + pago_mensual_ef 
    print(f'{mes_actual}:{round(total_pagado,2)}   {round(saldo,2)}')     
print(f'Mes:{mes_actual}\nResto a pagar:{round(saldo,2)}\nTotal pagado:{round(total_pagado, 2)}')
