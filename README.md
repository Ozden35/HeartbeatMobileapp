Nabız Takip ve Sınıflandırma Projesi

Proje Açıklaması

Bu proje, nabız ölçer cihazından alınan nabız verilerini yapay zeka kullanarak düşük, normal ve yüksek olarak sınıflandırmaktadır. Veriler, bir Firebase veritabanında saklanır ve bir server üzerinden yönetilir. Mobil uygulama, kullanıcıya anlık nabız durumunu bildirir.

Kullanılan Teknolojiler

Arduino (ESP8266 ve nabız ölçer sensörü)
Python & Scikit-Learn (Makine öğrenimi modeli)
Firebase (Veri tabanı)
Android (Kotlin) (Mobil uygulama)
Flask (Python) (Server yönetimi)

Çalışma Mantığı

Arduino, nabız ölçer sensöründen veri alır ve ESP8266 ile server'a gönderir.
Server (Flask), gelen veriyi yapay zeka modeline gönderir.
Yapay Zeka (Scikit-Learn & MLPClassifier), nabız verisini düşük, normal veya yüksek olarak sınıflandırır.
Firebase üzerinde bu veriler saklanır.
Android Uygulaması, Firebase üzerinden veriyi alarak kullanıcıya durum bilgisini gösterir.
