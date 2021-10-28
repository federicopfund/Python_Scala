#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
@author: Federico Pfund
 E-mail:federicopfund@gmail.com """
#%%
#  <--------------------------- Imports ---------------------------------->
import numpy as np
import pandas as pd
#%%
# <----------------------- Caminata al Azar ------------------------------>
idx = pd.date_range('20200923 14:00', periods = 120, freq = 'min')
s1 = pd.Series(np.random.randint(-1,2,120), index = idx)
s2 = s1.cumsum()
s2.plot()
#%%
# <----------------------Suavizar grafica -------------------------------->
w = 5 # ancho en minutos de la ventana
s3 = s2.rolling(w).mean()# Media Movil
s3.plot()# Grafico
#%%
# <-----------------------Graficos Ensimados ----------------------------->
df_series_23 = pd.DataFrame([s2, s3]).T  # armo un dataframe con ambas series
df_series_23.plot()
#%%
# <---------------Ejemplo de 12 personas caminando 8 horas---------------->
#Armamos también una lista de nombres.
horas = 8
idx = pd.date_range('20200923 14:00', periods = horas*60, freq = 'min')
nombres = ['Pedro', 'Santiago', 'Juan', 'Andrés','Bartolomé','Tiago','Isca','Tadeo','Mateo','Felipe','Simón','Tomás']
#%%
# <----------------- generar pasos para cada persona a cada minuto------->
df_walks = pd.DataFrame(np.random.randint(-1,2,[horas*60,12]).cumsum(axis=0),
                        index = idx, columns = nombres)
df_walks.plot()
#%%
# <---------------------- Suavizamos los Datos ------------------------->
# Usando min_periods para no perder los datos de los extremos.
w = 45
df_walk_suav = df_walks.rolling(w, min_periods = 1).mean() # datos suavizados
nsuav = ['S_' + n for n in nombres]
df_walk_suav.columns = nsuav # cambio el nombre de las columnas
                             # para los datos suavizados
df_walk_suav.plot()
#%%
# <------------------- Guardando datos en el disco --------------------->
df_walk_suav.to_csv('caminata_apostolica.csv')
#%%
