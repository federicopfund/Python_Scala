# -*- coding: utf-8 -*-
"""
Created on Wed Apr 28 14:51:04 2021

@author: feder
"""

# fileparse.py
import csv
#%%
def parse_csv(lines, select = None, types = None, has_headers = True, silence_errors = False):
    '''
    Parsea un archivo CSV en una lista de registros con conversión de tipos.
    '''
    if select and not has_headers:
        raise RuntimeError('para seleccionar columnas, el archivo tiene que tener encabezado')
    rows = csv.reader(lines)
    # Leer los encabezados (si hubiera)
    headers = next(rows) if has_headers else [] (edited)
