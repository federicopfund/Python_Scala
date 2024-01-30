#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
@author: Federico Pfund
 E-mail:federicopfund@gmail.com 
"""
#%%

# <------------------------- Imports ---------------------------------------->
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os
import sys
#%%
# <----------------------- Por Terminal -------------------------------------->
# por terminal:
""" python arbolado_parques_veredas.py 
    arbolado-publico-lineal-2017-2018.csv 
    arbolado-en-espacios-verdes.csv """
    
#%%
# <----------------------- Preguntas ---------------------------------------->
# 1) ¿Qué tendrías que cambiar para repetir el análisis para otras especies?
# 2) ¿Convendría definir una función?
# <-----------------------Respuestas ---------------------------------------->
# 1) Para poder analizar otras especies, deberia (en mi caso) modificar la
#     lista tipas para que obtenga datos con input()
# 
# 2) Seria bueno hacerlo con una funcion para separar el proceso del main.
#    Esto mismo se puede hacer con el resto de las listas!
#%%
# <----------------------- plot_dataset ------------------------------------->
def plot_dataset(dataset, variables):
    '''
    Plots de dataset generado
    '''
    dataset.boxplot(variables[0],by = 'ambiente')
    dataset.boxplot(variables[1],by = 'ambiente')
    plt.show()#grafico


# <--------------------------- datasets ------------------------------------->
def datasets(df_parques, df_veredas, tipas, cols):
    '''
    Creacion de dataset de arbolado en parques y veredas
    '''
    df_tipas_parques = df_parques[df_parques[cols[2]] == 
                                   tipas[0]][cols[0:3]].copy()
    
    df_tipas_parques = df_tipas_parques.rename(columns={cols[0]: 'altura', 
                                                        cols[1]: 'diametro',
                                                        cols[2]: 'nombre'})
    
    df_tipas_parques['ambiente'] = 'parque'
    df_tipas_veredas = df_veredas[df_veredas[cols[5]] == tipas[1]][cols[3:]].copy()
    df_tipas_veredas = df_tipas_veredas.rename(columns={cols[3]: 'altura',
                                                        cols[4]: 'diametro', 
                                                        cols[5]: 'nombre'})
    df_tipas_veredas['ambiente'] = 'vereda'

    df_tipas = pd.concat([df_tipas_veredas, df_tipas_parques])
    print(df_tipas)

    return df_tipas


# <----------------------------- Main --------------------------------------->
def main(f_veredas, f_parques):
    '''
    Programa principal
    '''
    directorio = '../Data/' # Indico directorio
    fname_v = os.path.join(directorio, f_veredas) # Busco el archivo
    df_veredas = pd.read_csv(fname_v) # Dataset de veredas
    fname_p = os.path.join(directorio, f_parques) # Busco el archivo
    df_parques = pd.read_csv(fname_p) # Dataset de parques

    tipas = ['Tipuana Tipu', 'Tipuana tipu']
    cols = ['altura_tot', 'diametro', 'nombre_cie', 'altura_arbol', 
                        'diametro_altura_pecho', 'nombre_cientifico']
    variables = ['diametro', 'altura']

    dataf = datasets(df_parques, df_veredas, tipas, cols)

    plot_dataset(dataf, variables)


# <---------------------------- Sys ----------------------------------------->
if __name__ == '__main__':
    try:
        if len(sys.argv) == 3: # Si paso tres argumentos, los guardo
            archivo1 = sys.argv[1]
            archivo2 = sys.argv[2]
        else: # Si no, entro por default al asignado
            archivo1 = 'arbolado-publico-lineal-2017-2018.csv'
            archivo2 = 'arbolado-en-espacios-verdes.csv'
    except FileNotFoundError:
        print(f'No se encuentran los archivos bebe,problemas con la ruta!!')
    main(sys.argv[0], sys.argv[1])
#%%