##Configuración del repositorio para el trabajo colaborativo

1) hacer fork del repositorio de líder: https://github.com/user-bcp-student24/wallet_4

2) clonar el repo en tu user (en mi caso es 19, hacer el cambio)
gh repo clone user-bcp-student19/wallet_4 &&
cd wallet_4 &&
pwd

3) Hacer remoto al repo del Lider
git remote get-url upstream >/dev/null 2>&1 || git remote add upstream https://github.com/user-bcp-student24/wallet_4.git
git remote -v

4) Copiar requeriments.txt de la carpeta crewai al nuevo repo:


5) Instalar el Python (las lineas hasta donde diga Enter):

[ -d .venv ] || python -m venv .venv

enter

if [ -f .venv/Scripts/activate ]; then
  source .venv/Scripts/activate
else
  source .venv/bin/activate
fi

enter

python -m pip install -r requirements.txt

enter 

[ -f .env ] || cp .env.example .env

6) crear una rama con el nombre de tu feature/nombre de tu usuario

ejemplo: feature/user-bcp-student19 

puedes hacerlo con este comando:

git checkout -b feature/user-bcp-student<numero>
