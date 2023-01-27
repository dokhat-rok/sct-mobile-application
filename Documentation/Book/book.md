Есть два способа заполучить объект класса weьview. Первый заключ;ается в использовании конструктора класса WebView, второй - в получении этого объекта из разметки приложения (предварительно его нужно поместить в разметку с помощью
визуального редактора разметки):
<br/>
// Первый способ
WebView browser = new WebView(this);
// Второй способ
WebView browser = (WebView) findViewByid(R.id.webview);
После этого можно загрузить документ:
browser.loadUrl("http://www.dkws.org.ua/");
При желании можно загружать НТМL-код из строки:
String html = "<htrnl><Ьody><hl>Hello</hl></body></htrnl>";
browser.loadData(html, "text/html", "utf-8");
д,1я настройки браузера используется класс weьsettings:
WebSettings webSettings = webView.getSettings();
// Блокируем картинки для экономии трафика
webSettings.setBlockNetworkimage(true);
// Запрещаем сохранять данные форм
webSettings.setSaveFoпnData(false);
// Разрешаем JavaScript
webSettings.setJavaScriptEnaЬled(true);
// Запрещаем сохранять пароли
webSettings.setSavePassword(false);
// Устанавливаем размер шрифта по умолчанию (от 1 до 72)
webSettings.setDefaultFixedFontSize(2);
// Устанавливаем название нашего браузера
webSettings.setUserAgentString("My browser v 1.0");
Подробно о методах класса weьsettings (а значит, и о параметрах браузера) вы можете прочитать в руководстве разработчика Android:
http://developer.android.com/reference/android/webkit/WebSettings.btml
Класс WebView тоже описан в руководстве разработчика:
bttp://developer .android.com/reference/android/webkit/W еЬ View .Ьtml 
