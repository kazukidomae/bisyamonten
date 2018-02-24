/**
 *
 */

// 左ドロワーメニューに表示させるロゴ画像を左ドロワーメニューに一斉に表示させる関数。
function ld_create()
{
	// window.alert("左ドロワーメニューへのステッカーロゴ画像追加処理");
	// ステッカージャンルの種類分ステッカーロゴ画像を左ドロワーメニューへ追加。
	for(i=1;i<=sg_size;i++)
	{
		$('#leftdrawer > ul').append('<li id="sgb' + i + '"><img src="images/sticker_img/logo' + i + '.png"></li>');
	}

	// 左ドロワーメニュー内にあるステッカージャンルのボタンのいずれか1つがクリックされたら、下ドロワーメニューに出すステッカーのキャラの種類を変更する処理を追加。
	$('#leftdrawer').find('li').on("click",function(){
		// 先ほどクリックされたステッカーのジャンルボタンのidに応じて、下ドロワーメニューに表示するステッカーの種類も変更する。
		// クリックされたステッカーのidを取得する変数。
		var click_stegen = $(this).attr('id');

		// 取得したidの値に応じて、下ドロワーメニューに表示するステッカーの種類を変更。
		// 現在dragareaのulタグ以下に入っている要素を全て一旦消去。
		$('#dragarea').empty();

		// for文によりステッカージャンルの種類数分判定処理を行う。
		for(i=1;i<=sg_size;i++)
		{
			// 下ドロワーメニューへ追加するステッカー画像は左ドロワーメニューでクリックされたステッカーのジャンルと関連付けられる。
			// なお、idを割り振る際にSSサイズはidの一番後ろにCを、LLサイズの場合は一番後ろにBを1文字追加する。
			if(click_stegen == "sgb" + i)
			{
				$('#dragarea').append('<li><img src="./images/sticker_img/st' + i + '.png" alt="ステッカーSS" class="sticker" id="st' + i + 'C" width="600px" height="600px" /></li>');
				$('#dragarea').append('<li><img src="./images/sticker_img/st' + i + '.png" alt="ステッカーS" class="sticker" id="st' + i + 'S" width="600px" height="600px" /></li>');
				$('#dragarea').append('<li><img src="./images/sticker_img/st' + i + '.png" alt="ステッカーM" class="sticker" id="st' + i + 'M" width="600px" height="600px" /></li>');
				$('#dragarea').append('<li><img src="./images/sticker_img/st' + i + '.png" alt="ステッカーL" class="sticker" id="st' + i + 'L" width="600px" height="600px" /></li>');
				$('#dragarea').append('<li><img src="./images/sticker_img/st' + i + '.png" alt="ステッカーLL" class="sticker" id="st' + i + 'B" width="600px" height="600px" /></li>');
			}
		}

		// 下ドロワーメニューに追加されたステッカー画像を展開図へドラッグ可能とする。
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

		// window.alert("下ドロワーメニューを変更しました！");
	});
}

// デザインページが読み込まれたら、左ドロワーメニューへのステッカージャンルのロゴ画像追加処理を実行する。
ld_create();

