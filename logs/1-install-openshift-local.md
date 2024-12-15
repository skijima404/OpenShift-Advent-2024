# OpenShift LocalのインストールからWebコンソールログインまで

## OpenShift Localのインストール

[Red Hat OpenShift Localのインストール](https://docs.redhat.com/ja/documentation/red_hat_openshift_local/2.2/html/getting_started_guide/installing_gsg)
インストーラー入手。

[インストーラーダウンロード](./images/1-1.png)

インストーラー実行するとウィザードが出るので次へ次へ。

[インストーラー実行](./images/1-2.png)

[インストーラー実行](./images/1-3.png)

[インストーラー実行](./images/1-4.png)

[インストーラー実行](./images/1-5.png)

[インストーラー実行](./images/1-6.png)

インストールのためにローカルのAdminパスワードを求められた。

[インストーラー実行](./images/1-7.png)

[インストーラー実行](./images/1-8.png)

## セットアップ

[Red Hat OpenShift Localのセットアップ](https://docs.redhat.com/ja/documentation/red_hat_openshift_local/2.2/html/getting_started_guide/setting-up_gsg)

セットアップ。ここはコマンドだけどコピペで良い。

マニュアルに「ホストの設定」と書いてあるけどこれは何をしているのか。
--> OpenShift Localは仮想マシンとして動作するので、仮想化エンジンとその上に乗る仮想マシンのセットアップをしている。
--> なお「crc」は元の名称であるCodeReady Containersから来ている
by ChatGPT (正確かは要確認)

```
crc setup
```

この後30分くらいかかった。5GB程度のものをダウンロードして展開していた様子。

[セットアップ](./images/1-9.png)

[セットアップ](./images/1-10.png)

## インスタンス開始

[インスタンスを開始します](https://docs.redhat.com/ja/documentation/red_hat_openshift_local/2.2/html/getting_started_guide/starting-the-instance_gsg)

```
crc start
```

[インスタンス開始](./images/1-11.png)

[インスタンス開始](./images/1-12.png)

最後にアクセス情報が出る。

> The server is accessible via web console at:
>   https://console-openshift-console.apps-crc.testing
> 
> Log in as administrator:
>   Username: kubeadmin
>   Password: 9jiuG-I7boD-nLZmg-3jSjQ
> 
> Log in as user:
>   Username: developer
>   Password: developer
> 
> Use the 'oc' command line interface:
>   $ eval $(crc oc-env)
>   $ oc login -u developer https://api.crc.testing:6443


## コンソールにアクセス

ブラウザでコンソールのURLにアクセス。
オレオレ証明書かな。
Advanced > Accept the Risk and Continue を2回ほどクリック。

[Warning](./images/1-13.png)

ログイン画面。

[Login](./images/1-14.png)

developerでログイン。

[developer home](./images/1-15.png)


以上、ターミナルでコマンド2回叩いた。