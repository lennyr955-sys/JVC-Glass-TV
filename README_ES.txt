JVC GLASS TV — PROYECTO ANDROID (VERSIÓN INICIAL)

IMPORTANTE
Este ZIP contiene el PROYECTO FUENTE para abrirlo en Android Studio. No es todavía un APK listo para instalar. En este entorno no está instalado Android SDK/Gradle, por lo que no puedo compilar ni verificar un APK real aquí.

DISPOSITIVO DETECTADO
- JVC 4K SMART TV
- Android 14
- Launcher original detectado: com.google.android.tvlauncher

QUÉ INCLUYE ESTA VERSIÓN INICIAL
- Aplicación de inicio en formato horizontal.
- Diseño oscuro con paneles translúcidos estilo glass.
- Lista las aplicaciones instaladas que Android TV expone como iniciables.
- Accesos a ajustes del sistema, Wi-Fi, Bluetooth y control de volumen.
- Acceso para volver a abrir el launcher original de Android TV.
- Menú visual de entradas HDMI/AV.

LIMITACIONES IMPORTANTES
- El cambio real entre HDMI/AV depende del firmware y de APIs privadas del fabricante. Esta versión lo informa claramente y NO finge cambiar las entradas.
- No reemplaza ni desinstala el launcher original.
- No incluye imágenes dinámicas de películas/series ni catálogo de streaming.
- No cambia ajustes protegidos del sistema.
- Android TV puede mostrar un selector para elegir el launcher predeterminado.

CÓMO GENERAR EL APK EN WINDOWS
1. Instala Android Studio desde https://developer.android.com/studio
2. Descomprime este ZIP.
3. Abre Android Studio > Open y selecciona la carpeta JVC_Glass_TV_Launcher.
4. Espera a que Gradle termine la sincronización.
5. Si Android Studio pide instalar Android SDK Platform 35 y Build Tools, acepta.
6. Menú Build > Build Bundle(s) / APK(s) > Build APK(s).
7. Android Studio mostrará la ruta del APK generado (normalmente app/build/outputs/apk/debug/app-debug.apk).

INSTALACIÓN POR POWERSHELL (SOLO CUANDO EXISTA EL APK)
Desde la carpeta platform-tools:
.db.exe install -r "RUTA_COMPLETApp-debug.apk"

Para probar sin hacerlo launcher predeterminado, abre la app desde Aplicaciones.
Para volver al inicio original, usa el botón incluido o el botón HOME y elige Android TV.
No ejecutes comandos para deshabilitar o borrar com.google.android.tvlauncher.
