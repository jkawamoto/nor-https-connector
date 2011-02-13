HttpsConnectorPlugin
=================================

概要
---------------------------------
安全でない http プロトコルでの通信を自動的に https プロトコルへと切り替えます．
現在対応しているドメインは次の通りです．（ * はワイルドカード）
- www.facebook.com
- www.google.com
- twitter.com
- www.evernote.com
- www.dropbox.com
- www.rememberthemilk.com

https プロトコルを用いて接続要求を行っても，
サーバ側がすべてのデータを https プロトコルを用いて送ってくるとは限らないことに注意してください．


コンパイル
---------------------------------
Maven2 を用いてこのプロジェクトをコンパイルするためには，
次のプラグインが必要です．
- serviceloader-maven-plugin: https://github.com/francisdb/serviceloader-maven-plugin

