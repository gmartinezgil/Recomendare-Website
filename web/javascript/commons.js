//********************************
//*********BUSY IMAGE*************
//********************************
 //var mask;

 function setupFunc() {
   //mask = new Wicket.Window.Mask(false);
   document.getElementsByTagName('body')[0].onclick = clickFunc;
   hideBusysign();
   Wicket.Ajax.registerPreCallHandler(showBusysign);
   Wicket.Ajax.registerPostCallHandler(hideBusysign);
   Wicket.Ajax.registerFailureHandler(hideBusysign);
 }

 function hideBusysign() {
   //mask.hide();
   document.getElementById('bysy_indicator').style.display ='none';
 }

 function showBusysign() {
   //mask.show();
   document.getElementById('bysy_indicator').style.display ='inline';
 }

 function clickFunc(eventData) {
   var clickedElement = (window.event) ? event.srcElement : eventData.target;
   if (clickedElement.tagName.toUpperCase() == 'BUTTON' || clickedElement.tagName.toUpperCase() == 'A' || clickedElement.parentNode.tagName.toUpperCase() == 'A'
     || (clickedElement.tagName.toUpperCase() == 'INPUT' && (clickedElement.type.toUpperCase() == 'BUTTON' || clickedElement.type.toUpperCase() == 'SUBMIT'))) {
     showBusysign();
   }
 }

//********************************
//*********HOTSPOT****************
//********************************
	/**
	 * @author David Pardo: Corunet
	 * Run after loading
	 */
 	function getMouseXY(e) {
 		var xOffset,yOffset;
 		var tempX = 0;
 		var tempY = 0;
 		
 		var myWidth = 0, myHeight = 0;
 		
 		//detect browser
		var IE = document.all?true:false
		if (!IE) {
			document.captureEvents(Event.MOUSEMOVE)
		}
		//find the position of the first item on screen and store offsets
		//find the first item on screen (after body)
		var firstElement=document.getElementsByTagName('body')[0].childNodes[1];
		//find the offset coordinates
		xOffset=findPosX(firstElement);
		yOffset=findPosY(firstElement);
		if (IE){ // In IE there's a default margin in the page body. If margin's not defined, use defaults
			var marginLeftExplorer  = parseInt(document.getElementsByTagName('body')[0].style.marginLeft);
			var marginTopExplorer   = parseInt(document.getElementsByTagName('body')[0].style.marginTop);
			/*assume default 10px/15px margin in explorer*/
			if (isNaN(marginLeftExplorer)) {marginLeftExplorer=10;}
			if (isNaN(marginTopExplorer)) {marginTopExplorer=15;}
			xOffset=xOffset+marginLeftExplorer;
			yOffset=yOffset+marginTopExplorer;
		}
		
		//get the size of the browsers window
		if( typeof( window.innerWidth ) == 'number' ) {
		  //Non-IE
		  myWidth = window.innerWidth;
		  myHeight = window.innerHeight;
		} else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
		  //IE 6+ in 'standards compliant mode'
		  myWidth = document.documentElement.clientWidth;
		  myHeight = document.documentElement.clientHeight;
		} else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
		  //IE 4 compatible
		  myWidth = document.body.clientWidth;
		  myHeight = document.body.clientHeight;
		}
		
		if (IE) {
			tempX = event.clientX + document.body.scrollLeft
			tempY = event.clientY + document.body.scrollTop
		} else {
			tempX = e.pageX
			tempY = e.pageY
		}
		tempX-=xOffset;
		tempY-=yOffset;
		var url='/recomendare/saveclick?x='+tempX+'&y='+tempY+"&w="+myWidth+"&h="+myHeight+"&l="+location.pathname;
		guardar(url);
		return true;
	}
 
	
	/*Find positions*/
	function findPosX(obj){
		var curleft = 0;
		if (obj.offsetParent){
			while (obj.offsetParent){
				curleft += obj.offsetLeft
				obj = obj.offsetParent;
			}
		}else if (obj.x){
			curleft += obj.x;
		}
		return curleft;
	}
	 
	function findPosY(obj){
		var curtop = 0;
		if (obj.offsetParent){
			while (obj.offsetParent){
				curtop += obj.offsetTop
				obj = obj.offsetParent;
			}
		}else if (obj.y){
			curtop += obj.y;
		}
		return curtop;
	}
	
	/*save data*/
	function guardar(url){
		var xmlDoc = null ;
		if (typeof window.ActiveXObject != 'undefined' ) {
			xmlDoc = new ActiveXObject('Microsoft.XMLHTTP');
		}else {
			xmlDoc = new XMLHttpRequest();
		}
		xmlDoc.open( 'GET', url, true );
		xmlDoc.send( null );
	}