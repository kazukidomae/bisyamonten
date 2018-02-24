// 現在表示中の痛車の3Dモデルを画像としてキャプチャするための関数。
// 引数にはストレージに保存する際のキーとなるファイル名をセットしてください。
function cap3D(s_filename)
{
	// ジャンルごとのステッカーをカウントする変数に、サイズ別にカウントするオブジェクトのインスタンスを代入する。
	for(i=1;i<scbg.length;i++)
	{
		scbg[i] = new SteCount();
	}

	// IDから取得したオブジェクトのエレメントを格納する変数。
	var objele;

	// クライアントPCに保存する痛車のキャプチャ画像のファイル名。同時にストレージに保存されているファイルの読み出し・削除にも使用する。
	var savefilename = s_filename;

	// 描画元となるキャンバス(webglにより描画されている領域)の情報を取得して変数に格納する。
	// var canvas_moto = document.getElementById("carcap");
	// var context_moto = canvas_moto.getContext('experimental-webgl',{preserveDrawingBuffer:true});

	// 描画先となるキャンバスの情報を取得して変数に格納する。
	var canvas_saki = document.getElementById("carcap");
	var context_saki = canvas_saki.getContext("experimental-webgl",{preserveDrawingBuffer: true});

	// 描画先のキャンバスのアドレスを格納。
	var cap_dataurl;

	// 展開図に配置されたステッカーのジャンル別のカウント処理。
	// ステッカージャンルの種類数分処理を繰り返す。
	for(i=1;i<=sg_size;i++)
	{
		for(j=1;j<ste_genlist.length;j++)
		{
			if(ste_genlist[j] == i)
			{
				// SSサイズのカウント。
				if(ste_sizelist[j] == "SS")
				{
					scbg[i].ss++;
				}
				// Sサイズのカウント。
				else if(ste_sizelist[j] == "S")
				{
					scbg[i].s++;
				}
				// Mサイズのカウント。
				else if(ste_sizelist[j] == "M")
				{
					scbg[i].m++;
				}
				// Lサイズのカウント。
				else if(ste_sizelist[j] == "L")
				{
					scbg[i].l++;
				}
				// LLサイズのカウント。
				else if(ste_sizelist[j] == "LL")
				{
					scbg[i].ll++;
				}
			}
		}
	}

	for(i=1;i<scbg.length;i++)
	{
		// window.alert("ステッカー" + i + "番のSSサイズ" + scbg[i].ss + "枚。");
		// window.alert("ステッカー" + i + "番のSサイズ" + scbg[i].s + "枚。");
		// window.alert("ステッカー" + i + "番のMサイズ" + scbg[i].m + "枚。");
		// window.alert("ステッカー" + i + "番のLサイズ" + scbg[i].l + "枚。");
		// window.alert("ステッカー" + i + "番のLLサイズ" + scbg[i].ll + "枚。");
	}

	// 先ほどカウントした展開図内のステッカーの枚数データを、サーブレットに送信させる。
	// なお、カウントの結果0枚だったステッカーに関しては、リクエストでサーブレットに情報を送らない。
	for(i=1;i<=sg_size;i++)
	{
		// ステッカーSSサイズ
		if(scbg[i].ss >= 1)
		{
			$('#st_count_info').append('<input type="hidden" name="ste' + i + 'SS" value="' + scbg[i].ss + '">');
		}
		// ステッカーSサイズ
		if(scbg[i].s >= 1)
		{
			$('#st_count_info').append('<input type="hidden" name="ste' + i + 'S" value="' + scbg[i].s + '">');
		}
		// ステッカーMサイズ
		if(scbg[i].m >= 1)
		{
			$('#st_count_info').append('<input type="hidden" name="ste' + i + 'M" value="' + scbg[i].m + '">');
		}
		// ステッカーLサイズ
		if(scbg[i].l >= 1)
		{
			$('#st_count_info').append('<input type="hidden" name="ste' + i + 'L" value="' + scbg[i].l + '">');
		}
		// ステッカーLLサイズ
		if(scbg[i].ll >= 1)
		{
			$('#st_count_info').append('<input type="hidden" name="ste' + i + 'LL" value="' + scbg[i].ll + '">');
		}
	}

	// var testimg = new Image();
	// testimg.src = "./janken_gu.png";
	// 画像がロードされたら実行する処理。
	// testimg.onload = function() {

		// 描画先キャンバスに背景色(赤)を描画。
		// context_saki.fillStyle = 'rgb(255,0,0)';
		// context_saki.fillRect(0,0,640,480);

		//キャンバスに画像の描画
		// context_saki.drawImage(testimg,0,0);

		// 描画先キャンバスに痛車のモデルを描画。
	//	context_saki.drawImage(canvas_moto,0,0);
	// }

	// 描画先キャンバスへ描画された内容を、canimgのsrcとして追加。
	cap_dataurl = canvas_saki.toDataURL();

	// ローカルストレージへ指定した名前でキャプチャデータを保存。
	window.localStorage.setItem(s_filename,cap_dataurl);

	// 痛車の画像を格納するimgタグを格納する。
	// ホームページ上に痛車のキャプチャ画像を追加。
	$('body').append('<img id="itasya" src="' + cap_dataurl + '">');

	// window.alert("痛車の3Dモデルをキャプチャしました！");

	// 先ほどカウントしたステッカーの枚数情報をデータベースに格納する処理を行う。その後再びdesign.jspへ遷移させる。
	document.st_count_info.submit();

}

// 各ステッカーのSS・S・M・L・LLの各サイズの貼られた枚数を記録するクラス。
function SteCount()
{
	this.ss = 0;
	this.s = 0;
	this.m = 0;
	this.l = 0;
	this.ll = 0;
	return this;
}