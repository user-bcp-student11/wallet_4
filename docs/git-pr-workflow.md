# Git PR Workflow

## 1. Publicar cambios en la rama de trabajo

Verificar cambios:

```bash
git status
```

Agregar cambios al staging:

```bash
git add .
```

Crear commit:

```bash
git commit -m "<descripción del commit>"
```

Publicar la rama:

```bash
git push -u origin feature/user-bcp-student<numero>
```

Obtener el hash del commit:

```bash
git rev-parse HEAD
```

---

## 2. Crear Pull Request

1. Ir al repositorio remoto.
2. Seleccionar **Create Pull Request**.
3. Completar la información requerida.
4. Enviar el PR para revisión.

---

## 3. Solicitar aprobación

1. Notificar al líder del equipo.
2. Solicitar revisión del código.
3. Esperar aprobación y Merge del Pull Request.

---

## 4. Sincronizar el Fork

Una vez realizado el Merge:

1. Ir al repositorio personal.
2. Hacer clic en **Update Branch** o **Synchronize Fork**.

---

## 5. Actualizar la rama local `main`

```bash
git switch main
git pull
```

---

## 6. Sincronizar `main` con la rama de trabajo

Cambiar a la rama feature:

```bash
git switch feature/user-bcp-student<numero>
```

Fusionar cambios de `main`:

```bash
git merge main
```

Si existen conflictos, resolverlos y luego publicar los cambios:

```bash
git push
```

---

## Flujo Resumido

```text
feature/user-bcp-student<numero>
        │
        ├─ git add .
        ├─ git commit
        ├─ git push
        ▼
    Pull Request
        ▼
   Revisión Líder
        ▼
       Merge
        ▼
   Sync Fork
        ▼
 git switch main
 git pull
        ▼
git switch feature/user-bcp-student<numero>
git merge main
git push
```