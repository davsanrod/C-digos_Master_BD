import requests
import json
import sys
from datetime import datetime

##Comprobar error
if len(sys.argv) != 2:
        print(f"Usage: {sys.argv[0]} <product> <dir>")
        exit(1)
DIR = sys.argv[1]

##Login - bicicletas
url = 'https://openapi.emtmadrid.es/v2/mobilitylabs/user/login/'
headers = {'email': 'davsanrod95@gmail.com', 'password': 'icemd411'}
response = requests.get(url, headers=headers)

##Token
data=response.json()
token = data['data'][0]["accessToken"]

##Desired Data 1
directoriBIC = '/home/dsanchez/BiciMadApi/bicicletas'
url1 = 'https://openapi.emtmadrid.es/v1/transport/bicimadgo/bikes/availability/'
headers = {'accessToken':token}
response = requests.get(url1, headers = headers)

##Comprobar conexion API 1
if response.status_code == 200:
        content = json.loads(response.content)
        dataBIC = content['data']
        fecha = datetime.now()
        fechafor = datetime.strftime(fecha, "&d%m%y_%H_%M_%S")
        with(open(f"{directoriBIC}/datos_{fechafor.now()}json","w")) as file:
                for line in dataBIC:
                        file.write(json.dumps(line, ensure_ascii=False))
                        file.write("\n")
else:
        print(f"Ha habido un error al llamar a la API: {response.status_code}")


##Desired Data 2 - estaciones (no hace falta volver a logearse)
directorioSTA = '/home/dsanchez/BiciMadApi/estaciones'
url2 = 'https://openapi.emtmadrid.es/v1/transport/bicimad/stations/'
response2 = requests.get(url2, headers = headers)


##Comprobar conexion API 2
if response2.status_code == 200:
        content = json.loads(response.content)
        dataSTA = content['data']
        fecha = datetime.now()
        fechafor = datetime.strftime(fecha, "&d%m%y_%H_%M_%S")
        with(open(f"{directorioSTA}/datos_{fechafor.now()}json","w")) as file:
                for line in dataSTA:
                        file.write(json.dumps(line, ensure_ascii=False))
                        file.write("\n")
else:
        print(f"Ha habido un error al llamar a la API: {response.status_code}")
