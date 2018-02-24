<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <%
    	// リクエスト取得時の文字コードを設定
    	request.setCharacterEncoding("UTF-8");

    	// 現在Cookieに格納されているユーザーIDを格納する変数。
    	String UserID = "";
    	// 現在のページ上部に表示させる、現在ログイン中のユーザーなどの情報を表すメッセージ。
    	String mes = "";

   		// 処理中で発生した例外の種類を格納する変数
   		// 0:例外発生なし　1:ファイルの読み込み失敗　2:ファイルの書き込み失敗　3:ファイルアウトプットストリームのクローズ失敗
   		int err = 0;
   		// index.jspの画面上に表示させるエラーメッセージの内容。
   		String errmsg = "";
   		// ブラウザバッグの処理を行いたい場合にHTML上に記述させるjavascript。
   		String jsmsg = "";

   		// 展開図に配置された各ステッカーのx座標を格納するアレイリスト。
   		ArrayList<Integer> stex = new ArrayList<Integer>();
   		// 展開図に配置された各ステッカーのy座標を格納するアレイリスト。
   		ArrayList<Integer> stey = new ArrayList<Integer>();
   		// 保存前に展開図に配置した各ステッカーのサイズ情報を格納するアレイリスト。
		ArrayList<String> stesize = new ArrayList<String>();
   		// 保存前に展開図に配置した各ステッカーのジャンル情報を格納するアレイリスト。
   		ArrayList<Integer> stegen = new ArrayList<Integer>();
   		// 保存前に展開図に配置した各ステッカーの回転角度情報を格納するアレイリスト。
   		ArrayList<Integer> sterot = new ArrayList<Integer>();

   		// 先ほど保存したばかりのデザインの作品番号を格納する変数。
   		int workID = 0;
   		// 画像のキャプチャ関数を実行する際の引数に使う文字を格納。
   		// workIDが取得できた場合は"cap1"、"cap2"のようにcap + workIDの連番の形とし、取得できなかった場合は"itasya"の名前で保存する。
		String cap_fname = "itasya";

   		// リクエストから受け取った痛車の車体の色情報を格納する変数。
   		String syatai_color = "";

   		// 例外の種類のリクエストが取得できる場合は取得する。
   		if(request.getAttribute("err") != null)
   		{
   			err = (Integer)request.getAttribute("err");
   			// 取得した例外の種類情報に応じて画面に表示させるエラーメッセージを分岐させる。
   			switch(err)
   			{
   				case 0:
   					errmsg = errmsg + "<p>例外の発生は特にありません。</p>";
   					break;
   				case 1:
   					errmsg = errmsg + "<p>ファイルの読み込み失敗の例外があります！</p>";
   					break;
   				case 2:
   					errmsg = errmsg + "<p>ファイルの書き込み失敗の例外があります！</p>";
   					break;
   				case 3:
   					errmsg = errmsg + "<p>ファイルアウトプットストリームのクローズ失敗の例外があります！</p>";
   					break;
   			}
   		}

   		// リクエストに作品番号の情報が存在する場合は、リクエストから取得する。
   		if(request.getAttribute("workID") != null)
   		{
   			workID = (Integer)request.getAttribute("workID");
   			// cap3D関数実行時の引数を変更。
   			cap_fname = "cap" + String.valueOf(workID);
   		}

   		// リクエストにステッカーのx座標情報がある場合は、リクエストからx座標のリストを取得。
   		if(request.getAttribute("stex") != null)
   		{
   			stex = (ArrayList<Integer>)request.getAttribute("stex");
   		}
   		// リクエストから貼り付けステッカーのy座標リストを取得。
		if(request.getAttribute("stey") != null)
		{
			stey = (ArrayList<Integer>)request.getAttribute("stey");
		}
   		// リクエストから貼り付けステッカーのサイズリストを取得。
   		if(request.getAttribute("stesize") != null)
		{
			stesize = (ArrayList<String>)request.getAttribute("stesize");
		}
   		// リクエストから貼り付けステッカーのジャンル情報を取得。
   		if(request.getAttribute("stegen") != null)
		{
			stegen = (ArrayList<Integer>)request.getAttribute("stegen");
		}
   		// リクエストから貼り付けステッカーの回転角度情報を取得。
   		if(request.getAttribute("sterot") != null)
   		{
   			sterot = (ArrayList<Integer>)request.getAttribute("sterot");
   		}

   		// リクエストにステッカーのサイズ情報がある場合は、リクエストから取得する。

   		// 現在ブラウザでCookieが何かしら格納されているかを調べ、格納されていたらユーザーIDが入っているCookieを探す。
   		if(request.getCookies() != null)
   		{
   			// 保存されているCookieを取得する。
   			Cookie cookies[] = request.getCookies();
   			for(int i=0;i<cookies.length;i++)
   			{
   				if(cookies[i].getName().equals("UserID"))
   				{
   					// クッキーにユーザーID情報が格納されていた場合は、クッキーからユーザーID情報を取得する。
   					UserID = cookies[i].getValue();
   				}
   			}
   			// ユーザーIDのcookieに何も値が入っていない場合、直近30分以内にログインしていないものとみなし、ログイン画面へ誘導するメッセージを出力する。
   			if(UserID.equals(""))
   			{
   				// mes = "<p>サービスをご利用の際は、ログインをお願いします。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
   				// 自動的にログイン画面へ遷移させる。
   				%>
   					<jsp:forward page="login.jsp" />
   				<%
   			}
   			// ユーザーIDのcookieに何か値が入っている場合、直近30分以内にログインしたとみなし、メインページの画面を表示する。
   			else
   			{
   				mes = "ようこそ:　" + UserID + "さん";
   			}
   		}
   		else
   		{
   			// Cookie自体が存在しない場合は、ログインしていないとみなし、強制的にログイン画面へ遷移させる。
   			%>
   				<jsp:forward page="login.jsp" />
   			<%
   		}


   		// 取得したx座標・y座標のデータ数、各ステッカーのジャンル情報、サイズ情報に応じて、展開図上にデフォルトでステッカーを表示させる処理を行う。
   		for(int i=0;i<stex.size();i++)
   		{
   			log( String.valueOf(stex.get(i)) );
   			log( String.valueOf(stey.get(i)) );
   			log( String.valueOf(stesize.get(i)) );
   			log( String.valueOf(stegen.get(i)) );
   			// x座標・y座標・ジャンル情報・サイズ情報・回転角度情報5つ全ての同じ要素番号でいずれかでもnullが格納されている場合は、ステッカー表示処理を実行しない。
   			if(stex.get(i) != null && stey.get(i) != null && stesize.get(i) != null && stegen.get(i) != null && sterot.get(i) != null)
   			{
   				// ページ読み込み時に自動実行されるjavascriptにステッカーの展開図追加処理を挿入。
   				jsmsg = jsmsg + "setSte(" + String.valueOf(stex.get(i)) + "," + String.valueOf(stey.get(i)) + ",\"" + String.valueOf(stesize.get(i)) + "\"," + String.valueOf(stegen.get(i)) + "," + String.valueOf(sterot.get(i)) + "); ";
   			}
   		}

   		// リクエストにブラウザバック処理情報が存在した場合は、自動で1つ前のページに戻る処理を行う。
   		//if(request.getAttribute("broBack") != null)
   		//{
   		//	jsmsg = "history.go(-1);";
   		//}

   		// リクエストから痛車の車体色情報が取得できる場合は取得し、展開図の色を指定した車体色に反映するjavascriptを実行する。
   		if(request.getAttribute("syatai_color") != null)
   		{
   			syatai_color = (String)request.getAttribute("syatai_color");
   			// javascript側で展開図の色を変更する処理を実行する。
   			jsmsg = jsmsg + "change_syatai(" + syatai_color + "); ";
   		}


    %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<title>美車紋店 デザイン</title>

	<!-- リセットCSSの読み込み -->
	<link href="./css/reset.css" rel="stylesheet" type="text/css" />

	<!-- CSSの読み込み -->
	<link href="./css/nav.css" rel="stylesheet" type="text/css" />
	<link href="./css/design.css" rel="stylesheet" type="text/css" />
	<link href="./css/drawer.css" rel="stylesheet" type="text/css" />
	<link href="./css/footer.css" rel="stylesheet" type="text/css" />
	<link href="./css/3d_modal.css" rel="stylesheet">

	<!-- ビューポートの設定 -->
	<!-- モーダルウィンドウ -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- 3D -->
	<meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

	<!-- javascriptの読み込み(画像処理) -->
	<script type="text/javascript" src="./script/jquery-3.0.0.min.js"></script>
	<script type="text/javascript" src="./script/jquery-ui.min.js"></script>
	<script type="text/javascript" src="./script/imgdrag.js"></script>

	<!-- javascriptの読み込み(モーダルウィンドウ処理) -->
	<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script> -->
	<script type="text/javascript" src="./script/modal.js"></script>

	<!-- javascriptの読み込み(3Dモデルキャプチャ関連) -->
	<script type="text/javascript" src="./script/capture.js"></script>

</head>
<body>

	<!-- ヘッダー -->
	<header>
	<!-- ロゴ -->
	<a href="top.html"><img src="image/rogo.png"></a>
	<!-- 仮ログアウトボタン -->
	<form action="MainPage" method="post">
		<input type="submit" id="logout" name="S_Logout" value="ログアウト" />
	</form>
		<!-- グローバルナビ -->
		<nav class="navigation_wrapper">
		<!-- デザイン用ボックス -->
			<div></div>
			<!-- メニュー -->
			<ul class="navigation">
				<li><a href="top.jsp">トップ</a></li>
				<li><a href="design.jsp">デザイン</a></li>
				<li><a href="gallery">ギャラリー</a></li>
				<li><a href="mypage">マイページ</a></li>
			</ul>
		</nav>
	</header>

	<!-- コンテンツエリア -->
	<div class="wapper">
		<!-- 左ドロワーメニュー -->
		<div id="leftdrawer">
			<ul>
			</ul>
		</div>
		<!-- 左ドロワーメニュー2 -->
		<div id="leftdrawer2">
			<ul>
				<!-- 痛車車体の色変更ボタン群 -->
				<li><input type="button" id="t0_button" value="白" onclick="change_syatai(0)"></li>
				<li><input type="button" id="t1_button" value="赤" onclick="change_syatai(1)"></li>
				<li><input type="button" id="t2_button" value="黒" onclick="change_syatai(2)"></li>
				<li><input type="button" id="t3_button" value="緑" onclick="change_syatai(3)"></li>
				<li><input type="button" id="t4_button" value="青" onclick="change_syatai(4)"></li>
				<li><input type="button" id="t5_button" value="オレンジ" onclick="change_syatai(5)"></li>
				<li><input type="button" id="t6_button" value="黒" onclick="change_syatai(6)"></li>
				<li><input type="button" id="t7_button" value="黒" onclick="change_syatai(7)"></li>
				<li><input type="button" id="t8_button" value="黒" onclick="change_syatai(8)"></li>
				<li><input type="button" id="t9_button" value="黒" onclick="change_syatai(9)"></li>
				<li><input type="button" id="t10_button" value="黒" onclick="change_syatai(10)"></li>
			</ul>
		</div>

		<!-- 下ドロワーメニュー -->
		<div id="bottomdrawer">
			<ul id="sizearea">
				<li>SS</li>
				<li>S</li>
				<li>M</li>
				<li>L</li>
				<li>LL</li>
			</ul>
			<ul id="dragarea">
			</ul>
		</div>

		<div id="menu-background"></div>

		<!-- 展開図 -->
		<div class="tenkaizu_wapper">
			<div id="tenkaizu">
			</div>
		</div>

		<!-- ゴミ箱 -->
		<!--<div id="dustbox">
		</div>-->

		<!-- 3D,保存ボタン -->
		<div id="button">
			<!-- 3D表示ボタン -->
			<input type ="button" id="modal-open" class="button-link" value="プレビュー">
			<!-- 保存ボタン  -->
			<form name="formarea" id="formarea" action="Jv2d" method="post">
				<input type="button" value="編集内容の保存" onClick="kakikae()">
			</form>
			<!-- 回転ボタン -->
			<input type="button" id="rot_button" value="右回転" onClick="rotateRight()">
			<!-- 痛車車体の色変更ボタン群 -->
			<!--   <input type="button" id="t0_button" value="色変更(白)" onclick="change_syatai(0)"> -->
			<!--  <input type="button" id="t1_button" value="色変更(赤)" onclick="change_syatai(1)"> -->
			<!--  <input type="button" id="t2_button" value="色変更(黒)" onclick="change_syatai(2)"> -->
		</div>


		<!-- モーダルウィンドウ -->
		<div id="modal-content">
			<input type="button" id="modal-close" name="return" value="×">
			<input type="button" name="toMygallery" id="capbutton" value="マイギャラリーに追加" onclick="cap3D('<%= cap_fname %>')">
				<div id="info" class="contentsize">

					<!-- 3D表示領域 -->
					<div id="3dload"></div>

				</div>
			</div>
		</div>
		<canvas id="canv" width="800" height="800" hidden></canvas>

		<!-- ページ読み込み時に自動的に実行されるjavascript -->
		<script type="text/javascript">
			$(document).ready(
				function(ev){
					<%= jsmsg %>
				}
			);
		</script>

		<!-- javascriptの読み込み(3D処理) -->
		<script type="text/javascript" src="./build/three.js"></script>
		<script type="text/javascript" src="./js/loaders/DDSLoader.js"></script>
		<script type="text/javascript" src="./js/loaders/MTLLoader.js"></script>
		<script type="text/javascript" src="./js/loaders/OBJLoader.js"></script>
		<script>
			var container, stats;
			var camera, scene, renderer;
			var mouseX = 0, mouseY = 0;
			var windowHalfX = window.innerWidth / 2;
			var windowHalfY = window.innerHeight / 2;

			// 現在マウスボタンが押されている場合はtrue、そうでない場合はfalseになるフラグ。
			var mousebut = false;

			init();
			animate();



			function init() {
				var onError = function ( xhr ) { };

				THREE.Loader.Handlers.add( /\.dds$/i, new THREE.DDSLoader() );

				var mtlLoader = new THREE.MTLLoader();
				mtlLoader.setBaseUrl( 'obj/male02/' );
				mtlLoader.setPath( 'obj/male02/' );
				mtlLoader.load( 'vezel_1201.mtl', function( materials ) {

				materials.preload();

				var objLoader = new THREE.OBJLoader();
				objLoader.setMaterials( materials );
				objLoader.setPath( 'obj/male02/' );
				objLoader.load( 'vezel_1201.obj', function ( object ) {

					object.position.set(0,0,0);
					object.scale.set(2,2,2.5);
					scene.add( object );

				}, onProgress, onError );

			});

			container = document.createElement( 'div' );
			container.draggable = true;
			document.getElementById('3dload').appendChild( container );

			// scene

			scene = new THREE.Scene();

			camera = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, 1, 1000 );
			camera.position.set(0,0,145);

			var ambient = new THREE.AmbientLight( 0x444444 );
			scene.add( ambient );

			var directionalLight = new THREE.DirectionalLight( 0xffffff );
			directionalLight.position.set( 0, 0.7, 0.7 ).normalize();
			scene.add( directionalLight );

			// model
			var onProgress = function ( xhr ) {
				if ( xhr.lengthComputable ) {
					var percentComplete = xhr.loaded / xhr.total * 100;
					console.log( Math.round(percentComplete, 2) + '% downloaded' );
				}
			};


			renderer = new THREE.WebGLRenderer({  preserveDrawingBuffer: true });
			renderer.setPixelRatio( window.devicePixelRatio );
			// renderer.setSize( window.innerWidth, window.innerHeight );
			renderer.setSize( 640,420 );
			//レンダラーの背景色の設定
			renderer.setClearColor( new THREE.Color(0xffffff) );
			renderer.domElement.setAttribute("id","carcap");
			container.appendChild( renderer.domElement );

			document.addEventListener( 'mousedown', onDocumentMouseDown, false );
			document.addEventListener( 'mouseup', onDocumentMouseUp, false );

			window.addEventListener( 'resize', onWindowResize, false );

		}

		function onWindowResize() {
			windowHalfX = window.innerWidth / 2;
			windowHalfY = window.innerHeight / 2;
			camera.aspect = window.innerWidth / window.innerHeight;
			camera.updateProjectionMatrix();
			renderer.setSize( window.innerWidth, window.innerHeight );
		}

		function onDocumentMouseMove( event ) {
			mouseX = ( event.clientX - windowHalfX ) / 2;
			mouseY = ( event.clientY - windowHalfY ) / 2;
		}

		function onDocumentMouseDown( event ){
			// マウスが動いている間実行するイベントを新たに追加する。
			document.addEventListener( 'mousemove', onDocumentMouseMove, false );
		}

		function onDocumentMouseUp( event ){
			// 現在設定されているマウスが動いている間実行するイベントを削除する。
			document.removeEventListener( 'mousemove', onDocumentMouseMove, false );
		}

		function animate() {
			requestAnimationFrame( animate );
			render();
		}

		function render() {
			camera.position.x += ( mouseX - camera.position.x ) * .05;
			camera.position.y += ( - mouseY - camera.position.y ) * .05;
			camera.lookAt( scene.position );
			renderer.render( scene, camera );
		}
	</script>
	<!-- 左ドロワーメニューから選ばれたステッカージャンルに応じて下ドロワーメニューに表示するステッカー画像を変更するjs。 -->
	<script type="text/javascript" src="./script/stechange.js"></script>
	<!-- キャプチャ画面から「マイギャラリーに追加」ボタンが押された時に、展開図のステッカーのカウント情報をサーブレットに送るためのフォーム。 -->
	<form id="st_count_info" name="st_count_info" action="AddGallery" method="post">
	</form>

</body>
</html>