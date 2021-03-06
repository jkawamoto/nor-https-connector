HttpsConnectorPlugin version 0.2.1
=================================

概要
---------------------------------
安全でない http プロトコルでの通信を自動的に https プロトコルへと切り替えます．
初期状態で対応しているドメインは次の通りです．
- www.facebook.com
- www.google.com
- twitter.com
- www.evernote.com
- www.dropbox.com
- www.rememberthemilk.com

設定ファイルは，nor.plugin.HttpsConnectorPlugin.conf で，
    <httpsプロトコルへ切り替えるホスト>=<httpsでのホスト名>
という形式で記述します．
例えば，
    www.facebook.com=ssl.facebook.com
という記述があった場合，http://www.facebook.com 以下へのリクエストはすべて，
https://ssl.facebook.com 以下へリダイレクトされるようになります．

https プロトコルを用いて接続要求を行っても，
サーバ側がすべてのデータを https プロトコルを用いて送ってくるとは限らないことに注意してください．
例えば，あるサイトが別ドメインにあるファイルをリンクしている場合，
そのファイルに対する要求は https 通信にならないかも知れません．


使い方
---------------------------------
ダウンロードした，HttpsConnectorPlugin.jar を plugin フォルダに追加してください．


ライセンス
---------------------------------------
本ソフトウェアは、GNU 一般公衆利用許諾書(GNU GPL)バージョン 3 のもとで配布されています。

このプログラムはフリーソフトウェアです．
あなたはこれを，フリーソフトウェア財団によって発行されたGNU 一般公衆利用許諾書(バージョン3か、それ以降のバージョンのうちどれか)
が定める条件の下で再頒布または改変することができます．

このプログラムは有用であることを願って頒布されますが，全くの無保証です．
商業可能性の保証や特定目的への適合性は，言外に示されたものも含め，全く存在しません．
詳しくはGNU 一般公衆利用許諾書をご覧ください．