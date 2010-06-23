HttpsConnectorPlugin version 0.1
=================================

概要
---------------------------------
安全でない http プロトコルでの通信を自動的に https プロトコルへと切り替えます．
現在対応しているドメインは次の通りです．（ * はワイルドカード）
- www.facebook.com
- *.google.com
- twitter.com
- www.evernote.com
- www.dropbox.com
- www.rememberthemilk.com
- www.paypal.*

https プロトコルを用いて接続要求を行っても，
サーバ側がすべてのデータを https プロトコルを用いて送ってくるとは限らないことに注意してください．



使い方
---------------------------------
ダウンロードした，HttpsConnector.jar を plugin フォルダに追加してください．