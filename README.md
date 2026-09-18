# TwoPersonMessenger Android V2 Security

Server: https://hesabat.site/wp/api/

## Bu build-də
- Launcher adı: **Sənədlər**
- İlk ekran real PDF sənəd ekranıdır.
- `Qovluq seç` ilə Android Storage Access Framework vasitəsilə istifadəçi seçdiyi
  telefon qovluğuna oxuma icazəsi verir.
- Seçilən qovluq və alt qovluqlardakı real PDF-lər göstərilir və açılır.
- `Sənədlər` başlığına **2 dəfə sürətli toxunmaq** gizli Messenger keçididir.
- İlk girişdə username + server PIN tələb olunur.
- Sessiya varsa gizli keçiddən Messenger açılır.
- Messenger ekranlarında FLAG_SECURE: screenshot/screen recording və Recent Apps
  önizləməsinin qarşısını alır.
- Ayarlar > Təhlükəsizlik: çıxışdan sonra lokal tarixçənin görünməməsi üçün
  1/5/15/30/60 dəqiqə seçimi.
- Serverdə mesajlar SİLİNMİR.
- Təmizləmə vaxtı keçdikdən sonra APK `cleared_before` saxlayır və serverdən
  yalnız həmin vaxtdan sonrakı mesajları göstərir.
- Mövcud mətn mesajlaşması, heartbeat, read status və mesaj silmə API-ləri saxlanılıb.
- Səsli/video zəng düymələri hələ WebRTC mərhələsi üçün placeholder-dır.

## Server patch vacibdir
`TwoPersonMessenger_SERVER_V2_SECURITY_PATCH.zip` paketindəki `api/messages.php`
faylını serverdə `public_html/wp/api/messages.php` ilə əvəz edin.
SQL import lazım deyil.

## GitHub build
Repo kökünə bütün faylları upload edin.
Actions -> Build Android APK -> Run workflow.
Artifact: TwoPersonMessenger-debug / app-debug.apk
