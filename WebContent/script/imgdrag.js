// ステッカーの画像をドラッグと連動して移動させるためのjavascript。

// ここから　変数宣言・初期化スペース
// ドラッグ終了後に対象のオブジェクトを元の位置に戻すかの情報が格納されるフラグ。
var drflg = true;

var cnt = 1;

// カウンタ変数の定義
var i = 0;
var j = 0;

// 現在移動中のステッカーの絶対x座標を格納する。
var giste_x = 0;
// 現在移動中のステッカーの絶対y座標を格納する。
var giste_y = 0;

// 現在ブラウザ上で表示中のページのスクロール位置。
var p_scroll;

// ステッカーを赤く変色させた画像データが格納されているパス。
var dataurl;

// 現在ステッカーの色が赤く変色している場合にtrueとなるフラグ。
var steredflg = false;
// 現在ステッカーが展開図領域の下端からはみ出ている場合にtrueとなるフラグ。
var hami_shita = false;

// 現在移動中の展開図に配置済みのステッカーの移動前x座標を記録する変数。
var ste_idoumae_x = 0;
// 現在移動中の展開図に配置済みのステッカーの移動前y座標を記録する変数。
var ste_idoumae_y = 0;

// これから追加予定のステッカーのIDを格納する変数。
var tsuika_ste = "";
// 現在選択中のステッカーのIDを格納する変数。
var senntaku_ste = "";

// 展開図に貼り付け中の各ステッカーの現在の回転角度を格納する配列変数。要素番号はステッカーのid(ste)と連動する。なお、要素番号0は未使用とする。
// 格納される値と回転角度‥‥‥　0:0度　1:90度　2:180度　3:270度
var ste_rotlist = [];
// 配列要素番号0番に予めダミーデータをセット。
ste_rotlist.push(0);
// 展開図に貼り付け中の各ステッカーのジャンルを格納する配列変数。要素番号はステッカーのid(ste)と連動する。要素番号0は未使用とする。
var ste_genlist = [];
// ダミーデータのセット。
ste_genlist.push(0);
// 展開図に貼り付け中の各ステッカーのサイズ情報を格納する配列変数。要素番号はステッカーのid(ste)と連動。要素番号0は未使用とする(ダミーデータの挿入)。
// アレイリストに格納される値‥‥‥　"SS","S","M","L","LL","ALL"の6種類のサイズ。
var ste_sizelist = [];
ste_sizelist.push("ダミー");

// ステッカーを展開図に配置する際の展開図上で表示されるステッカーのサイズを格納する変数。
// 展開図に配置されるステッカーの幅。
var ste_width = 0;
// 展開図に配置されるステッカーの高さ。
var ste_height = 0;

// 3Dプレビューボタンのクリック有効化フラグ。false:3Dボタンのクリック無効　true:3Dボタンのクリック有効。　デフォルト値はtrue。
var preclickok = true;

// ステッカージャンルの種類数を格納する変数。
var sg_size = 22;
// 展開図に貼られたステッカーのジャンル判定用で用いる、サイズ情報のアルファベット1文字を抜いた判定対象のステッカーのidを格納する変数2つ。
var sg_hanntei = "";
var sg_hanntei2 = "";

// ステッカーidごとのステッカー名を格納するリスト配列。
var stenamelist = ["ダミー","ピカチュウ","マリオ","アリサ","ファイナルファンタジー"];

//各ジャンルごとのステッカーの枚数を記録する配列変数。要素番号はステッカーのidと連動する。なお要素番号0番は未使用とする。
var scbg = new Array((sg_size + 1));

// 現在保存前の回転角度再現用の処理が実行中の場合はtrue、そうでなければfalseを格納するフラグ。
var rot_saigenn = false;

// 保存前の回転角度再現処理で、必要な回転回数(引数の値)を一時的に保管する変数。
var rot_need = 0;

// 現在選択中の車体の色を格納する変数。　　0:白　1:赤　2:黒
var syatai_now = 0;

// ここまで　変数宣言・初期化スペース

//最初に展開図に配置されたステッカーがクリックされた時の処理。
jQuery(
	function(){
		// 最初にHTML上に配置されている「右回転」ボタンを無効化し、ボタン背景色の色を暗くする。
		$('#rot_button').attr('disabled',true);
		$('#rot_button').attr('cursor','default');
		$('#rot_button').css('background','#303030');
		// 同時に、車体の色選択ボタンの「白」を暗くして無効化する。
		$('#t0_button').attr('disabled',true);
		$('#t0_button').css('background','#303030');

		// ステッカーの画像の色彩を赤くするテスト。
		// ste_red();
		// ステッカーの画像をドラッグ操作1回でコピーできるようにする。
		$('#dragarea').find('img').draggable(
			{
				opacity:0.3,
				helper:'clone',
				revert:'false',
				drag:function(e,ui){
					// 現在移動中のステッカーの位置に応じて赤く変色させる関数を実行。
					// ste_hanntei(e,ui);
				}
			}
		);
		$('#tenkaizu').droppable(
			{
				accept:'.sticker',
				tolerance:'fit',
				drop:function(e,ui){
					// ステッカーの一覧リストから展開図に持ってきた場合に限り、ステッカーを複製する。
					if(ui.draggable.attr("class") == "sticker ui-draggable ui-draggable-handle" && steredflg == false)
					{
						// 追加予定のステッカーに割り振るIDを設定する。
						tsuika_ste = "ste" + cnt + "";

						// 貼られたステッカーのidを取得し、ステッカーのサイズを表すidの末尾のアルファベット1文字を取得、サイズに応じてクローンの大きさを変更する。

						// サイズSSの場合は、幅と高さを200pxに設定する。
						if(ui.draggable.attr("id").substr(-1,1) == "C")
						{
							ste_width = 200;
							ste_height = 200;
							// ステッカーサイズリストに"S"を追加。
							ste_sizelist.push("SS");
						}
						// サイズSの場合は、幅と高さを300pxに設定する。
						else if(ui.draggable.attr("id").substr(-1,1) == "S")
						{
							ste_width = 300;
							ste_height = 300;
							// ステッカーサイズリストに"S"を追加。
							ste_sizelist.push("S");
						}
						// サイズMの場合は、幅と高さを400pxに設定する。
						else if(ui.draggable.attr("id").substr(-1,1) == "M")
						{
							ste_width = 400;
							ste_height = 400;
							// ステッカーサイズリストに"M"を追加。
							ste_sizelist.push("M");
						}
						// サイズLの場合は、幅と高さを500pxに設定する。
						else if(ui.draggable.attr("id").substr(-1,1) == "L")
						{
							ste_width = 500;
							ste_height = 500;
							// ステッカーサイズリストに"L"を追加。
							ste_sizelist.push("L");
						}
						// サイズLLの場合は、幅と高さを600pxに設定する。
						else
						{
							ste_width = 600;
							ste_height = 600;
							// ステッカーサイズリストに"LL"を追加。
							ste_sizelist.push("LL");
						}

						// 貼られたステッカーのidのサイズ部(S,Mなど)1文字より前の文字列を全て抜き出し、ジャンル判定の処理を行う。
						sg_hanntei = ui.draggable.attr("id");
						sg_hanntei2 = sg_hanntei.substr(0,sg_hanntei.length - 1);
						// ステッカージャンルの種類数分処理を繰り返す。
						for(i=1;i<=sg_size;i++)
						{
							if(sg_hanntei2 == "st" + i)
							{
								// ステッカーidのi番のステッカーの場合
								// window.alert("" + stenamelist[i] + "のステッカー");
								ste_genlist.push(i);
							}
						}

						ui.draggable.clone().attr({'id':tsuika_ste,'class':'shiyouzumi','width':ste_width,'height':ste_height}).css({'top':(ui.offset.top - $('#tenkaizu').position().top),'left':(ui.offset.left - $('#tenkaizu').position().left),'z-index':cnt }).appendTo(this)
						.on("mousedown",{value:tsuika_ste},kaiten_hy).draggable(
						{
							opacity:0.3,
							helper:'original',
							revert:'false',
							start:function(e,ui){
								ste_idoumae_x = (ui.offset.left - $('#tenkaizu').position().left);
								ste_idoumae_y = (ui.offset.top - $('#tenkaizu').position().top);
							},
							drag:function(e,ui){
								// 現在移動中のステッカーの位置に応じて赤く変色させる関数を実行。
								// ste_hanntei(e,ui);
							},
							stop:function(e,ui){
								// ドラッグが終了した時、ステッカーが展開図の領域外に配置されていた(赤くなっている)場合は、移動前の位置へ戻す。
								///if(steredflg == true)
								///{
									// ステッカーの赤色フラグをfalseとする。
								//	steredflg = false;
									// 先ほどまで移動したステッカーの色を通常色へ戻す。
								//	ui.helper.attr("src","./images/sticker_img/IMG_0479.png");
									// 先ほどまで移動したステッカーを移動前の位置へ戻す。
								//	ui.helper.css({'top':ste_idoumae_y,'left':ste_idoumae_x});
								//}
							}
						});
						// ステッカーの回転角度リストに初期値(0度)を追加。
						ste_rotlist.push(0);
						for(i=0;i<ste_rotlist.length;i++)
						{
							// window.alert(ste_rotlist[i]);
						}
						cnt++;
					}
					// ステッカーリストからではなく、既に展開図上に配置されているステッカーを移動させた場合
					else if(steredflg == false)
					{
						// 回転ボタンの位置をステッカーの移動先の位置へ移動させる。
						// $('#rotatebut').css(
						// 	{
						//		'top':( ui.offset.top - $('#tenkaizu').position().top ),
						//		'left':( ui.offset.left - $('#tenkaizu').position().left + (ui.helper.width() - 95) )
						//	}
						//);
					}

					// ステッカーの編集がされた場合は、3Dプレビューのボタンを無効化し、編集内容の保存をしないとプレビューを見れないようにする。
					predisabled();

				}
			}
		);
		// ステッカーをこの上でドロップすると自動的に消去できるゴミ箱エリア。
		$('#dustbox').droppable(
			{
				accept:'.sticker',
				tolerance:'fit',
				drop:function(e,ui){
					if(ui.draggable.attr("id").match(/^ste\d+$/))
					{
						ui.draggable.remove();
					}
				}
			}
		);
	}

);

// 現在ブラウザ上で表示されているページのスクロール位置を取得する関数。
function GetScrollPosition(document_obj)
{
	return{
		x:document_obj.body.scrollLeft || document_obj.documentElement.scrollLeft,
		y:document_obj.body.scrollTop  || document_obj.documentElement.scrollTop
	};
}

function kakikae()
{
	// ここから　変数宣言・初期化スペース

	// 展開図上に配置されているステッカーの一覧リスト配列。
	var stelist = $('#tenkaizu').find('img');
	// window.alert(stelist.length);
	// 展開図上に配置されているステッカーのx座標。
	var stelist_x = new Array(stelist.length);
	// 展開図上に配置されているステッカーのy座標。
	var stelist_y = new Array(stelist.length);
	// 展開図上に配置されているステッカーの幅を記録するリスト配列。
	var stelist_width = new Array(stelist.length);
	// 展開図上に配置されているステッカーの高さを記録するリスト配列。
	var stelist_height = new Array(stelist.length);
	// 展開図上に配置されているステッカー画像のファイルパス一覧。
	var stelist_src = new Array(stelist.length);
	// 展開図に配置されたステッカーの枚数を格納する変数。
	var ste_count = stelist.length;

	// ここまで　変数宣言・初期化スペース

	// 展開図上に配置されているステッカーの数分処理を繰り返す。
	for(i=0;i<stelist.length;i++)
	{
		// 展開図上のステッカーのx座標を算出。
		stelist_x[i] = Math.floor( $(stelist[i]).offset().left - $('#tenkaizu').position().left );
		// 展開図上のステッカーのy座標を算出。
		stelist_y[i] = Math.floor( $(stelist[i]).offset().top - $('#tenkaizu').position().top );
		// 展開図上のステッカーの幅をリストに格納。
		stelist_width[i] = parseInt( $(stelist[i]).attr('width') );
		// 展開図上のステッカーの高さをリストに格納。
		stelist_height[i] = parseInt( $(stelist[i]).attr('height') );
		// 展開図上の画像のファイルパスを取得。
		stelist_src[i] = $(stelist[i]).attr('src');
		// window.alert($(stelist[i]).attr('src'));

		// フォームエリアに送信するステッカーのx座標情報をhiddenタグで追加。
		$('#formarea').append('<input type="hidden" name="stex' + i + '" value="' + stelist_x[i] + '">');
		// フォームエリアに送信するステッカーのy座標情報をhiddenタグで追加。
		$('#formarea').append('<input type="hidden" name="stey' + i + '" value="' + stelist_y[i] + '">');
		// フォームエリアに送信するステッカーの幅情報をhiddenタグで追加。
		$('#formarea').append('<input type="hidden" name="stew' + i + '" value="' + stelist_width[i] + '">');
		// フォームエリアに送信するステッカーの高さ情報をhiddenタグで追加。
		$('#formarea').append('<input type="hidden" name="steh' + i + '" value="' + stelist_height[i] + '">');
		// フォームエリアに送信するステッカーのファイルパス情報をhiddenタグで追加。
		$('#formarea').append('<input type="hidden" name="src' + i + '" value="' + stelist_src[i] + '">');
	}
	// フォームエリアに送信するステッカーの貼られた枚数情報をhiddenタグで追加。
	$('#formarea').append('<input type="hidden" name="ste_count" value="' + ste_count + '">');

	// 展開図に貼られた各ステッカーの回転角度情報をhiddenタグでリクエストに追加。
	for(i=1;i<ste_rotlist.length;i++)
	{
		$("#formarea").append('<input type="hidden" name="sterot' + i + '" value="' + ste_rotlist[i] + '" >');
	}
	// 展開図に貼られた各ステッカーのジャンル情報をhiddenタグでリクエストに追加。
	for(i=1;i<ste_genlist.length;i++)
	{
		$("#formarea").append('<input type="hidden" name="stegen' + i + '" value="' + ste_genlist[i] + '" >');
	}
	// 展開図に貼られた各ステッカーのサイズ情報をhiddenタグでリクエストに追加。
	for(i=1;i<ste_sizelist.length;i++)
	{
		$("#formarea").append('<input type="hidden" name="stesize' + i + '" value="' + ste_sizelist[i] + '" >');
	}

	// フォームエリアに痛車車体の色情報をhiddenタグで追加。
	$('#formarea').append('<input type="hidden" name="syatai" value="' + syatai_now + '">');

	// window.alert("ファイルの書き換え処理を実行しました。");
	// Jv2dサーブレットへ値を送信。
	window.setTimeout(function(){ $('#formarea').submit(); },1000);
}
function ste_red()
{
	//　ここから　変数宣言・初期化スペース

	// 画像の各画素の色情報更新処理で使うインデックス。
	var idx = 0;
	// 色情報更新処理で現在処理中の赤色画素の強さ。
	var red = 0.0;
	// 色情報更新処理で現在処理中の青色画素の強さ。
	var blue = 0.0;
	// 色情報更新処理で現在処理中の緑色画素の強さ。
	var green = 0.0;

	//　ここまで　変数宣言・初期化スペース

	// imgオブジェクトの作成
	var img = new Image();
	// 読み込み先画像ソース。
	img.src = "./images/sticker_img/IMG_0479.png";
	img.onload = function()
	{
		// 画像読み込みが終了後ここで画像を加工して表示。
		// キャンバスのオブジェクトを作成。
		var canv = document.createElement('canvas');
		var context = canv.getContext('2d');
		var img_w = img.width;
		var img_h = img.height;
		canv.width = $('#stedef').width();
		canv.height = $('#stedef').height();

		// window.alert(canv.width);
		// window.alert(canv.height);

		// キャンバス内にステッカー画像を描画。
		context.drawImage(img,0,0);

		// イメージデータを取得。
		var imgdata = context.getImageData(0,0,img_w,img_h);
		// canvasに描画させる予定のイメージデータを格納する。
		var dstdata = context.createImageData(img_w,img_h);
		var imd = imgdata.data;
		var dst = dstdata.data;

		// getImageDataで取得した画像の各画素のRGB値を書き換える。
		for(i=0;i<img_h;i++)
		{
			for(j=0;j<img_w;j++)
			{
				// インデックスの値を更新。
				idx = (j + i * img_w) * 4;
				red = imd[idx] // 赤色部分取得
				green = imd[idx + 1]; // 緑色部分取得
				blue = imd[idx + 2]; // 青色部分取得

				// 画像を赤色に色付ける。
				red = Math.floor((red / 255.0) * 255.0);
				green = Math.floor((green / 255.0) * 50.0);
				blue = Math.floor((blue / 255.0) * 50.0);

				// 処理中の画素のRGBデータを書き換え。
				dst[idx] = red;
				dst[idx + 1] = green;
				dst[idx + 2] = blue;
				dst[idx + 3] = imd[idx + 3];
			}
		}

		// キャンバスにdstdataの内容を描画する。
		context.putImageData(dstdata,0,0);

		// 画像タグへ代入し、実際に表示。
		dataurl = canv.toDataURL();
		$('#output').html("<img src='" + dataurl + "'>");
	}

}

// ステッカーを領域外に配置しようとしたらステッカー画像を赤く変色させる判定用関数。
function ste_hanntei(e,ui)
{

	// 現在移動中のステッカーのページ上の絶対x座標、y座標を取得する。
	giste_x = Math.floor(ui.offset.left - $('#tenkaizu').position().left);
	giste_y = Math.floor(ui.offset.top - $('#tenkaizu').position().top);

	// ページの下部に現在のマウスポインタの絶対座標x,yを表示する。
	$('#ste_x').text("" + steredflg);
	$('#ste_y').text("" + giste_y);

	// 展開図の配置可能領域内にステッカーを移動した場合は通常色で表示し、領域外に移動した場合は赤色で表示する。
	// 立方体の展開図の内左から1番目の列。
	if(giste_x >= -11 && giste_x < 154 && giste_y >= -12 && giste_y <= 834)
	{
		// ステッカーを通常色で表示。
		steredflg = false;
		ui.helper.attr("src","./images/sticker_img/IMG_0479.png");
	}
	// 立方体の展開図の内左から2番目の列。
	else if(giste_x >= 154 && giste_x < 494 && giste_y >= 331 && giste_y <= 834)
	{
		// ステッカーを通常色で表示。
		steredflg = false;
		ui.helper.attr("src","./images/sticker_img/IMG_0479.png");
	}
	// 立方体の展開図の内左から3番目の列。
	else if(giste_x >= 494 && giste_x < 836 && giste_y >= 668 && giste_y <= 834)
	{
		// ステッカーを通常色で表示。
		steredflg = false;
		ui.helper.attr("src","./images/sticker_img/IMG_0479.png");
	}
	// 上記の条件に全て当てはまらない場合は領域外と判断し、ステッカーを赤く変色させて表示させる。
	else
	{
		// ステッカーを赤く変色させて表示させる。
		steredflg = true;
		ui.helper.attr("src","" + dataurl + "");
	}

}

// 回転ボタンをステッカーの下部に表示させる関数(同時に選択中のステッカーの周りに赤線の枠を表示させる)。引数には回転ボタンを表示させたいステッカーのIDを入れてください。
function kaiten_hy(e)
{
	// 現在どのステッカーにも回転ボタンが表示されていない(選択中のステッカーがない)場合
	if(senntaku_ste == "")
	{
		// 現在選択中のステッカーを引数に入れられたステッカーIDとする。
		senntaku_ste = e.data.value;
		// 作業エリアの右側にある「右回転」ボタンを有効化し、ボタンをクリックすると右回転の処理を行えるようにする。
		$('#rot_button').attr('disabled',false);
		$('#rot_button').attr('cursor','pointer');
		$('#rot_button').css('background','#a99977');
		// 展開図上に回転ボタンの要素を追加する。
		// $('#tenkaizu').append('<div id="rotatebut"></div>');
		// 回転ボタンのCSSを操作し、対象ステッカーの下部に回転ボタンを表示させるようにする。
		// $('#rotatebut').css(
		//	{
		//		'top':( $('#' + e.data.value).offset().top - $('#tenkaizu').position().top ),
		//		'left':( $('#' + e.data.value).offset().left - $('#tenkaizu').position().left + ($('#' + e.data.value).width() - 95) )
		//	}
		//);
		// 選ばれたステッカーの周りに赤線の枠を表示させる。
		$('#' + e.data.value).css('border','solid 4px red');
		// 回転ボタンをクリックした時のイベントを追加する。
		//$('#rotatebut').on("mousedown",function(){
			// ステッカーの画像回転処理を実行する。
		//	rotateRight();
		//});
	}
	// 現在既に回転中ボタンが表示中のステッカーがある場合で、引数(選択対象)のステッカーIDとsenntaku_steが異なる場合
	else if(senntaku_ste != e.data.value)
	{
		// 回転ボタンの表示位置を新たに選択したステッカーに変更する。
		//$('#rotatebut').css(
		//	{
		//		'top':( $('#' + e.data.value).offset().top - $('#tenkaizu').position().top ),
		//		'left':( $('#' + e.data.value).offset().left - $('#tenkaizu').position().left + ($('#' + e.data.value).width() - 95) )
		//	}
		//);
		// 現在選択中のステッカーの赤線の枠を非表示、新たに選択されたステッカーの周りに赤線の枠を表示させる。
		$('#' + senntaku_ste).css('border','none');
		$('#' + e.data.value).css('border','solid 4px red');
		// 現在回転ボタンを表示中のステッカーIDを変更する。
		senntaku_ste = e.data.value;
	}
}

// 回転ボタンが押された時に、対象のステッカー画像を右に90度回転させるための関数。ただし、回転ボタンは一緒に回転しない。
function rotateRight()
{
	// window.alert("rotateRightを実行します！");
	// キャンバス領域の幅と高さを入れ替える際の一時記憶用変数。
	var csize_mem = 0;
	// ステッカー画像の幅
	var steimg_width = 0;
	// ステッカー画像の高さ
	var steimg_height = 0;
	// 回転させたステッカーのidの末尾の数字を格納する変数。
	var ste_matsubi = 0;
	// HTML上で表示させるステッカー画像の幅。
	var sennste_w = parseInt( $('#' + senntaku_ste).attr('width') );
	// HTML上で表示させるステッカー画像の高さ。
	var sennste_h = parseInt( $('#' + senntaku_ste).attr('height') );

	// キャンバスの現在の内容を変数に取得する。
	var canvas = document.getElementById("canv");
	// 取得したキャンバスエレメントからコンテキストを取得する。
	var context = canvas.getContext('2d');
	// キャンバスに描画する、ステッカーの画像情報を格納。
	var rotimg = new Image();
	// 画像のファイルパスを、現在選択中のステッカーの背景画像のファイルパスに設定。
	rotimg.src = $('#' + senntaku_ste).attr('src');
	// 画像がロードされたら以下の処理を実行する。
	rotimg.onload = function(){
		// canvasの幅・高さを該当ステッカーの幅・高さに合わせる。
		// 対象となるステッカー画像の幅・高さを取得。
		// window.alert(rotimg.width);
		// window.alert(rotimg.height);
		steimg_width = sennste_w;
		steimg_height = sennste_h;
		// window.alert(steimg_width + "あああ");
		// window.alert(steimg_height + "あああ");
		canvas.width = steimg_width;
		canvas.height = steimg_height;

		// ステッカーの回転描画処理を行う前に、原点の座標を画像の中心へ移動。
		context.translate(steimg_width * 0.5,steimg_height * 0.5);
		// ステッカーの画像を90度右回転させる。
		// 回転角度をラジアン値で指定する。
		var radian = 90 * Math.PI / 180;
		// 90度右回転。
		context.rotate(radian);

		// 画像がキャンバスからはみ出さないように、原点の位置を修正。
		context.translate(steimg_width * -0.5,steimg_height * -0.5);

		// window.alert("キャンバスへの画像描画前");

		// キャンバス上にステッカーの画像を描画。
		context.drawImage(rotimg,0,0,steimg_width,steimg_height);

		// window.alert("キャンバスへの画像描画後");

		// 90度の右回転が終了したらtoBlobを実行し画像をpngファイルへ変換する。
		canvas.toBlob(function(blob){
			//pngに変更後自動的にここが実行される
			//キャンバスに描画された内容を画像のソースとして取得する。
			var canurl = URL.createObjectURL(blob);
			// 現在選択中のステッカーの画像ソースをキャンバスのURLに設定する。
			// window.alert("pngへの画像変換成功！");
			// window.alert("回転後画像の画像パスは" + canurl);
			document.getElementById(senntaku_ste).src = canurl;
		});

		// toBlobを実行して展開図上のステッカー画像ファイルのsrcに反映させたら、後処理としてキャンバス領域の回転角度を元に戻す。
		// 原点の位置を再び画像の中心へ移動。
		context.translate(steimg_width * 0.5,steimg_height * 0.5);
		radian = -90 * Math.PI / 180;
		// 90度左回転。
		context.rotate(radian);
		// 画像が領域からはみ出さないよう、原点の位置を修正。
		context.translate(steimg_width * -0.5,steimg_height * -0.5);

		// 回転処理が終わったら、先ほど回転させた画像のidから末尾の数字を取得して、リストの該当の要素番号の回転角度情報を1プラスする。
		ste_matsubi = parseInt( senntaku_ste.substr(3) );
		ste_rotlist[ste_matsubi]++;
		if(ste_rotlist[ste_matsubi] >= 4)
		{
			ste_rotlist[ste_matsubi] = 0;
		}
	}
}

// 展開図上の指定位置にステッカーを追加する関数。
// 引数にはステッカーの貼り付け先となるx座標、y座標、ステッカーのサイズ情報、ステッカーのジャンル情報、ステッカー回転角度情報をセットしてください。
function setSte(stex,stey,stesize,stegen,sterot)
{
	// window.alert("回転角度:" + sterot);
	// 再現に必要な回転回数を一時的に変数に格納する。
	rot_need = sterot;

	// 追加するステッカーに割り振るid。
	tsuika_ste = "ste" + cnt + "";
	// window.alert(stesize);

	// 処理対象となっているステッカーのサイズを取得し、サイズに合わせて展開図に実際に表示させるステッカーの大きさを変更する。
	if(stesize == "SS")
	{
		// SSサイズの場合
		ste_width = 200;
		ste_height = 200;
		// ステッカーサイズリストに"SS"を追加。
		ste_sizelist.push("SS");
	}
	if(stesize == "S")
	{
		// Sサイズの場合
		ste_width = 300;
		ste_height = 300;
		// ステッカーサイズリストに"S"を追加。
		ste_sizelist.push("S");
	}
	else if(stesize == "M")
	{
		// Mサイズの場合
		ste_width = 400;
		ste_height = 400;
		// ステッカーサイズリストに"M"を追加。
		ste_sizelist.push("M");
	}
	else if(stesize == "L")
	{
		// Lサイズの場合
		ste_width = 500;
		ste_height = 500;
		// ステッカーサイズリストに"L"を追加。
		ste_sizelist.push("L");
	}
	else if(stesize == "LL")
	{
		// LLサイズの場合
		ste_width = 600;
		ste_height = 600;
		// ステッカーサイズリストに"LL"を追加。
		ste_sizelist.push("LL");
	}

	// 展開図に配置したステッカーのジャンルに応じて、同じステッカーを保存後にデフォルトで配置させる。
	$('#tenkaizu').append('<img src="./images/sticker_img/st' + stegen + '.png" alt="ステッカー" id="' + tsuika_ste + '" class="shiyouzumi" width="' + ste_width + '" height="' + ste_height + '">');
	$('#' + tsuika_ste).css({'top':stey,'left':stex,'z-index':cnt }).on("mousedown",{value:tsuika_ste},kaiten_hy).draggable(
	{
		opacity:0.3,
		helper:'original',
		revert:'false',
		start:function(e,ui)
		{
			ste_idoumae_x = (ui.offset.left - $('#tenkaizu').position().left);
			ste_idoumae_y = (ui.offset.top - $('#tenkaizu').position().top);
		},
		stop:function(e,ui)
		{
			// 予め自動配置されたステッカーを移動させてドロップした場合は、3Dプレビューボタンのクリックを無効化する。
			predisabled();
		}
	});

	// ステッカーのジャンルリストに現在処理中のステッカーのジャンルを追加。
	ste_genlist.push(stegen);

	// ステッカーの回転角度リストに初期値(0度)を追加。
	ste_rotlist.push(0);
	// 引数にセットされた回転角度情報を基に、新たに配置されるステッカーを保存前と同じだけ回転。
	// senntaku_ste = tsuika_ste;

	//for(i=0;i<sterot;i++)
	//{
	//	$.when(
	//		rotateRight()
	//	).done(
	//		function(){
	//			window.alert("回転処理終了。i=" + i + "です。");
	//		}
	//	);
	//}

	// 回転処理終了後の後片付け。
	// senntaku_ste = "";
	// ステッカーのID割り振り用のカウントを1プラスする。
	cnt++;
	// window.alert("cnt++ cnt=" + cnt);
}

// 3Dのプレビューボタンを無効化するメソッド。
function predisabled()
{
	$('#modal-open').attr('disabled',true);
	$('#modal-open').attr('cursor','default');
	// ボタンの背景色を少し暗くする。
	$('#modal-open').css('background-color','#303030');
	// 3Dボタン有効状態フラグをfalseにする。
	preclickok = false;
}

// 車体の色選択ボタンがクリックされた時に実行される関数。現在設定中の色のボタンは暗くして無効化する。
function change_syatai(syatai_to)
{
	var syatai_to_in = syatai_to;
	// 現在設定中の色用のボタンを有効化する。
	$('#t' + syatai_now + '_button').attr('disabled',false);
	$('#t' + syatai_now + '_button').css('background','#a99977');
	// 新しく設定予定の色用のボタンを無効化する。
	$('#t' + syatai_to_in + "_button").attr('disabled',true);
	$('#t' + syatai_to_in + "_button").css('background','#303030');
	// 現在設定中の色を新たな色に設定する。
	syatai_now = syatai_to_in;

	// 現在ブラウザ上に表示されている展開図を指定した色の展開図に差し替える。
	$('#tenkaizu').css('background-image','url(./tenkaizu/t' + syatai_to_in + '.png)');
}
