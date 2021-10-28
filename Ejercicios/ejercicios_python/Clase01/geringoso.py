dic = dict()

def geringoso(lista):
  
    try:  
        
        for gerin in lista:
            palabra_convertida = " "
            palabra_convertida += " "
            for goso in gerin:
               if goso in "aeiou":
                   palabra_convertida += goso + "p" + goso
               else:
                   palabra_convertida += goso
            dic [gerin] = palabra_convertida
        return (dic)
        
    except:
        print("no admite alfanumericos")
        
    
geringoso(['pera','mandarna','naranja'])
