# OpenAPI 生成

Apicurio Studioを使ってOpenAPIを生成する。

[Apicurio Studioのデモ環境](https://studio.apicur.io/) はあるんだけど、[Apicurio Studioのページ](https://www.apicur.io/studio/) からリンクが外れており、デモ環境のバージョンはDepricatedになっている。

せっかくなので環境を自前でOpenShift Localに立ててみよう。

## 1. OpenShift LocalにApicurio Studio用のProjectを作成

Developer権限だとプロジェクトを作れないので、kubeadminでログインし直す。

[Login](./images/2-1.png)

create a Project

[create a Project](./images/2-2.png)

プロジェクト名 apicurio

[project name](./images/2-3.png)

## 2. Apicurio Studioのコンテナイメージを取得

[Apicurio Studio - Getting Started](https://www.apicur.io/studio/getting-started/)

+Add --> Container Images

[container images](./images/2-4.png)

apicurio/apicurio-studio を指定して Create

[apicurio-studio](./images/2-5.png)

一瞬。

[apicurio studio running](./images/2-6.png)

もう一つ、Apicurio Studio UIのコンテナ作成。
apicurio/apicurio-studio-ui を指定して Create
General > Application にさっきの apicurio-studio が自動的に指定されている。ありがたい。

[apicurio-studio-ui](./images/2-7.png)

自動的にRouteが追加されている。

[apicurio studio ui running](./images/2-8.png)

アクセスしてみる。
400 bad request

[bad request](./images/2-9.png)

なぜかapicurio-studioの方もRouteがある。これも試してみる。

[404](./images/2-10.png)

何か足りない?
quay.io で検索するとapicurio-studioから始まるものがいくつかある。

[quay.io](./images/2-11.png)

調査

ダメ元でChatGPTに違いについて聞いたら、apicurio-studioの方が apicurio-studio-api + apicurio-studio-ui の機能が入っているものだそう。
引用元は見つからない。怪しいかもしれない。
削除してapicurio-studioだけ入れ直す、ダメならapicurio-studio-apiとapicurio-studio-uiを入れてみる。

削除。

Deployments から削除。
右上の三つの点からDelete

[deployments](./images/2-12.png)

ではもう一度

[apicurio-studio](./images/2-13.png)

同じエラー。
気になるのはこれJSONなんですよね。

[404](./images/2-14.png)

> {"message":"RESTEASY003210: Could not find resource for full path: http://apicurio-studio-apicurio.apps-crc.testing/","error_code":404,"detail":"NotFoundException: RESTEASY003210: Could not find resource for full path: http://apicurio-studio-apicurio.apps-crc.testing/","name":"NotFoundException"}

- なぜJSON? どこのエラー？
- エラーメッセージがhttpsではなくhttpになっている --> 多分途中が終端になっているんだと思うけど要確認
- Routeに公開されているURLがhttpsだけど、念の為httpにするとhttpsに変更される --> これ自体は問題なし

httpsの終端とポート不整合がないかチェックしてみる。

Route --> Service --> Pod

Route
[Route](./images/2-15.png)

>  host: apicurio-studio-apicurio.apps-crc.testing
>  to:
>    kind: Service
>    name: apicurio-studio
>    weight: 100
>  port:
>    targetPort: 8080-tcp

apicurio-studio-apicurio.apps-crc.testing に来たリクエストは apicurio-studio という Serviceの8080に転送。

>  tls:
>    termination: edge
>    insecureEdgeTerminationPolicy: Redirect

ここがTLSの終端になっている。

問題なさそう。

Service

[service](./images/2-16.png)

>  ports:
>    - name: 8080-tcp
>      protocol: TCP
>      port: 8080
>      targetPort: 8080
>    - name: 8443-tcp
>      protocol: TCP
>      port: 8443
>      targetPort: 8443
>    - name: 8778-tcp
>      protocol: TCP
>      port: 8778
>      targetPort: 8778
>    - name: 9779-tcp
>      protocol: TCP
>      port: 9779
>      targetPort: 9779

同じポート同士になっている。

>  selector:
>    app: apicurio-studio
>    deployment: apicurio-studio

app=apicurio-studio, deployment=apicurio-studioに転送

Pod一覧に出ているので単純にこれをみる。

[pod list](./images/2-17.png)

Pod

[pod](./images/2-18.png)

>      ports:
>        - containerPort: 8080
>          protocol: TCP
>        - containerPort: 8443
>          protocol: TCP
>        - containerPort: 8778
>          protocol: TCP
>        - containerPort: 9779
>          protocol: TCP

Pod側も同じポートが開いている。

[logs](./images/2-19.png)

> 2024-12-06 13:24:15 INFO [io.apicurio.common.apps.logging.audit.AuditLogService] (executor-thread-1) apicurio.audit action="request" result="failure" src_ip="null" path="/" response_code="404" method="GET" user="

リクエストはPodまで来てエラー起きたみたい。

これはちょっと自分だといかんともし難いなあ。

OpenShiftに入れるためのOperatorのパッケージがGithubにありそう。
これは複数コンポーネント構成になっている (-api, -uiなどなど)。
--> [Localに入れるとCORSで引っかかってアクセスできないエラー発見](https://github.com/Apicurio/apicurio-studio/issues/2745)

ここは諦めて大人しくオンラインにある古いデモバージョンを使うことにする。

## 3. OpenAPI作成

[Apicurio Studioのデモ環境](https://studio.apicur.io/)
これがあればAPIの仕様を作るときにOpenAPIの文法を覚えなくてもいい。

[Create New API](./images/2-20.png)

タスクAPIにしよう。

[Create a New API Design](./images/2-21.png)

Edit APIから編集画面へ。

[Edit API](./images/2-22.png)

Add a Pathから編集画面へ。

[Add a Path](./images/2-23.png)

[path](./images/2-24.png)

[path --> tasks](./images/2-25.png)

次はデータタイプ。

[data type](./images/2-26.png)

右側の画面が日切り替わるのでPropertiesに属性を追加する。

[properties](./images/2-27.png)

こんな感じ。

[data type property](./images/2-28.png)

2個目からは　右端のプラス (+) マークから。

[properties](./images/2-29.png)

次はResponseを追加。

[response](./images/2-30.png)

画面右側がまた切り替わるので、Body追加。

[task response](./images/2-31.png)

メディアタイプ追加。

[media type](./images/2-32.png)

「No Type」と書いてあるところをクリックし、Typeのプルダウンからさっき登録したtasksデータタイプを選択。

[no type](./images/2-33.png)

[type=tasks](./images/2-34.png)

Pathの/tasksに戻ってオペレーション追加。

[Operations](./images/2-35.png)

Add Operation > Add a Response

[add a response](./images/2-36.png)

200 OK に先ほどのレスポンスを設定

[response=tasks](./images/2-37.png)

この辺スクショ撮るのめんどくさくなったので割愛 

/tasks
/tasks/{id}

を定義。

できたものはエクスポートできる。

[taskapi](./images/2-38.png)

この画面に戻って右上の点みっつのアイコンからダウンロード。

[Task API](../openapi/Task%20API.yaml)

OpenAPIはこれでOK。
