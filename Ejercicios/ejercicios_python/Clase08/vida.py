#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
@author: Federico Pfund
 E-mail:federicopfund@gmail.com 
"""
#%%
#  <--------------------------- Imports --------------------------->
from datetime import datetime, date
#%%
# <------------------------- vida_en_seg -------------------------->
def vida_en_seg(nacimiento):
    fecha_hoy = date.today()
    fecha_nacimiento = date(year = nacimiento[0], month = nacimiento[1], day = nacimiento[2])
    segundos_vida = fecha_hoy - fecha_nacimiento
    print("Segundos de vida: ", segundos_vida.total_seconds())


# <------------------------ nacimiento --------------------------->
def nacimiento():
    dia = int(input("Dia: "))
    mes = int(input("Mes: "))
    anio = int(input("Añio: "))

    return (anio, mes, dia)


#  <------------------------- main ------------------------------>
def main():
    fecha_nacimiento = nacimiento()
    vida_en_seg(fecha_nacimiento)


#  <------------------------ sys ------------------------------->
if __name__ == "__main__":
    main()