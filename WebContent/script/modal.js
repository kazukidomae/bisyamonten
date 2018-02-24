$(function(){
//モーダルウィンドウを出現させるクリックイベント
$(".modal-open, #modal-open").click( function(){

	 var id = $(this).find("img").attr('src');

	$("#view img").attr('src',id);

	var wi = $(this).find('input[name="workId"]').val();
	console.log(wi);
	$('#Nc').val(wi);

	//ユーザーID
	$("#Id").text($(this).children("input[name='userId']").val());
	//ユーザーネーム
	$("#Name").text($(this).children("input[name='userName']").val());
	//作成日時
	$("#Data").text($(this).children("input[name='date']").val());
	//いいね数
	$("#Nice").text($(this).children("input[name='nice']").val());
	//お気に入り数
	$("#Favorite").text($(this).children("input[name='favorite']").val());



	//キーボード操作などにより、オーバーレイが多重起動するのを防止する
	$( this ).blur() ;	//ボタンからフォーカスを外す
	if( $( "#modal-overlay" )[0] ) return false ;		//新しくモーダルウィンドウを起動しない (防止策1)
	//if($("#modal-overlay")[0]) $("#modal-overlay").remove() ;		//現在のモーダルウィンドウを削除して新しく起動する (防止策2)

	//オーバーレイを出現させる
	$( "body" ).append( '<div id="modal-overlay"></div>' ) ;
	$( "#modal-overlay" ).fadeIn( "slow" ) ;

	//コンテンツをセンタリングする
	centeringModalSyncer() ;

	//コンテンツをフェードインする
	$( "#modal-content" ).fadeIn( "slow" ) ;

	//[#modal-overlay]、または[#modal-close]をクリックしたら…
	$( "#modal-overlay,#modal-close" ).unbind().click( function(){

		//[#modal-content]と[#modal-overlay]をフェードアウトした後に…
		$( "#modal-content,#modal-overlay" ).fadeOut( "slow" , function(){

			//[#modal-overlay]を削除する
			$('#modal-overlay').remove() ;

		} ) ;

	} ) ;

} ) ;

//リサイズされたら、センタリングをする関数[centeringModalSyncer()]を実行する
$( window ).resize( centeringModalSyncer ) ;

	//センタリングを実行する関数
	function centeringModalSyncer() {

		//画面(ウィンドウ)の幅、高さを取得
		var w = $( window ).width() ;
		var h = $( window ).height() ;

		// コンテンツ(#modal-content)の幅、高さを取得
		// jQueryのバージョンによっては、引数[{margin:true}]を指定した時、不具合を起こします。
//		var cw = $( "#modal-content" ).outerWidth( {margin:true} );
//		var ch = $( "#modal-content" ).outerHeight( {margin:true} );
		var cw = $( "#modal-content" ).outerWidth();
		var ch = $( "#modal-content" ).outerHeight();

		//センタリングを実行する
		$( "#modal-content" ).css( {"left": ((w - cw)/2) + "px","top": ((h - ch)/2) + "px"} ) ;

	}
} ) ;

//いいねボタン処理
$("#Nicebutton").click(function(){
	//console.log("aaaaa");

	var workid = $(':hidden[name="Nicecount"]').val();
	$.ajax({
		type: "GET",
		url: "CountUp",
		data:{
			'workid':workid,
		}
	}).done(function(response, textStatus, jqXHR) {
		var Nco = $('#Nice').text();
		Nco++;
		$('#Nice').text(Nco);
	  });
});
