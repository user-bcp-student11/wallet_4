# Configuración del Repositorio para Trabajo Colaborativo

## 1. Crear un Fork del repositorio del líder

Repositorio del líder:

https://github.com/user-bcp-student24/wallet_4

---

## 2. Clonar tu Fork

Reemplaza el número de usuario por el tuyo.

Ejemplo para el usuario 19:

```bash
gh repo clone user-bcp-student19/wallet_4 && \
cd wallet_4 && \
pwd
```

---

## 3. Configurar el repositorio del líder como upstream

Agregar el repositorio principal como remoto `upstream`:

```bash
git remote get-url upstream >/dev/null 2>&1 || \
git remote add upstream https://github.com/user-bcp-student24/wallet_4.git
```

Verificar la configuración de remotos:

```bash
git remote -v
```

Resultado esperado:

```text
origin    https://github.com/user-bcp-studentXX/wallet_4.git (fetch)
origin    https://github.com/user-bcp-studentXX/wallet_4.git (push)
upstream  https://github.com/user-bcp-student24/wallet_4.git (fetch)
upstream  https://github.com/user-bcp-student24/wallet_4.git (push)
```

---

## 4. Copiar el archivo de dependencias

Copiar el archivo `requirements.txt` desde la carpeta base del proyecto o desde el repositorio proporcionado para la práctica.

Verificar que exista:

```bash
ls requirements.txt
```

---

## 5. Configurar el entorno Python

### Crear el entorno virtual

```bash
[ -d .venv ] || python -m venv .venv
```

### Activar el entorno virtual

Git Bash / Windows:

```bash
if [ -f .venv/Scripts/activate ]; then
  source .venv/Scripts/activate
else
  source .venv/bin/activate
fi
```

### Instalar dependencias

```bash
python -m pip install -r requirements.txt
```

### Crear archivo de configuración local

```bash
[ -f .env ] || cp .env.example .env
```

---

## 6. Crear tu rama de trabajo

La rama debe seguir el formato:

```text
feature/user-bcp-student<numero>
```

Ejemplo:

```text
feature/user-bcp-student19
```

Crear y cambiar a la nueva rama:

```bash
git checkout -b feature/user-bcp-student<numero>
```

También puedes usar la sintaxis moderna de Git:

```bash
git switch -c feature/user-bcp-student<numero>
```

---

## Validación Final

Verificar que estés trabajando en tu rama feature:

```bash
git branch
```

Resultado esperado:

```text
* feature/user-bcp-student<numero>
  main
```

Verificar el estado del repositorio:

```bash
git status
```

Si no existen cambios pendientes, el repositorio estará listo para comenzar el desarrollo.