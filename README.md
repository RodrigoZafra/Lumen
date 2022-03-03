## Lumen
### Una aplicación para entender el sistema eléctrico español

Nuestra aplicación trata de informar a los usuarios interesados en el funcionamiento y los problemas de nuestro actual modelo de generación y consumo eléctrico. Para ello vamos a desarrollar dos funcionalidades básicas. Por un lado, una serie de artículos divulgativos vinculados entre ellos y, por otro, una muestra gráfica de los datos que obtendremos de la API de la <a href="https://www.ree.es/es/apidatos">Red Eléctrica Española<a>.

Al abrir la aplicación nos encontramos con el **SplashScreen** formado por el logo y el nombre de la aplicación, "LUMEN". Unos segundos después llegaremos automaticamente (tras finalizar la animación) al **MainActivity**, que será un pequeño menú donde podemos elegir los dos apartados fundamentales: Producción y consumo.

1. ¿Cómo funciona?

  - Contendrá unos pequeños infoSlides (diapositivas) que explicarán nociones básicas sobre el sistema eléctrico, generación de energía, mercado eléctrico, impacto ambiental...
  
2. Impacto Ecológico - Mercado y Consumo

  - Estos apartados mostrarán estadísticas de sus datos correspondientes como podemos ver en la cuarta imágen de la infografía. Vamos a desarrollar
  una **Tabbed Activity** dividida en tres fragments que mostrarán las pestañas en las que se filtrarán las estadísticas por: dia, mes y año.
    
  Mario Castro, Tirso García y Rodrigo Zafra.
