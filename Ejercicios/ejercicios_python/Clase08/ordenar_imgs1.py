# -*- coding: utf-8 -*-
"""

@author: feder
"""
#%%

import os
import re
import datetime
#%%

def buscar_imgs(directorio):
    '''Recibe un directorio, busca y procesa archivos que contengan
    la expresion nombre_yyyymmdd.png
    Procesar implica:
        1- Mover a un nuevo directorio
        2- Cambiar la fecha de u. modificacion a la escrita en el nombre
        3- Eliminar el _yyyymmdd del nombre
        4- Eliminar los contenedores vacios luego del procesamiento anterior
    '''
    png_regex = re.compile(r'(.*)(_)(\d{8})(\.png)$')
    for root, folder, names in os.walk(directorio):
        for file in names:
            if png_regex.match(file):
                print(os.path.join(root, file))
                procesar_img(os.path.join(root, file))
                

def procesar_img(ruta):
    '''Proceso de imagenes.
    pre: ruta de un archivo con ultimos digitos de la forma _yyyymmdd.png
    pos: se mueve el archivo a nuevo directorio, con fecha de u. modif cambiada
    a la fecha del nombre del archivo, esta fecha es borrada del nombre.'''
    
    fecha_regex = re.compile(r'.*\\(.*)(_)(\d{8})(\.png)$')
    
    fecha = fecha_regex.sub(r'\3', ruta) # fecha del documento
    
    fecha_modif = datetime.datetime.strptime(fecha, '%Y%m%d')
    
    ts_acceso = datetime.datetime.now().timestamp()
    ts_modif = fecha_modif.timestamp() # seteo marca de tiempo modificacion
    
    os.utime(ruta, (ts_acceso, ts_modif)) #seteo fecha modificacion
       
    n_nombre = fecha_regex.sub(r'\1\4', ruta) # se elimina '_fecha' del nombre
    
    os.rename(ruta, os.path.join('../Data/', 'imgs_procesadas', n_nombre))
    

def limpiar_dirs():
    '''Se limpian carpetas vacias.'''
    ordenar_tree = os.path.join('../Data/', 'ordenar')
    for root, folder, names in os.walk(ordenar_tree, topdown=False):
        if not len(names):
            print(f'Se elimino {root}')
            os.rmdir(root)


        
def main(directorio):
    os.mkdir(os.path.join('../Data/', 'imgs_procesadas'))
    if len(directorio) != 2:
        raise SystemExit('Se debe ingresar una ruta.')
    else:
        buscar_imgs(directorio)
        limpiar_dirs()
   
if __name__ == '__main__':
    import sys
    main(sys.argv[1])  
    print('¡Ordenamiento Terminado!')
    