

def neutralizar_palabras(palabra):
    if len(palabra)>=2 and palabra[-2]=='0':
        palabra_nueva=palabra[:-2] +'e'+ palabra[-1]
    elif palabra[-1]=='o':
        palabra_nueva[:-1] + 'e'
    else:
        palabra_nueva=palabra
        
    return palabra_nueva

frase = '¿como trasmitir a los otros el infinito Aleph?'
palabras = frase.split()
palabras_nuevas= []
for palabra in palabra:
    palabra_nueva = neutralizar_palabras(palabra)
    palabra_nueva.append(palabra_nueva)
frase_t =' '.join(palabra_nueva)
print(frase_t)