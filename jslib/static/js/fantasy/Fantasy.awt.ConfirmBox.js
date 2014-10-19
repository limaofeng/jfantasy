//widget
//triggers	放置的位置
//positionAdjust 	[]	top left 
//onConfirm 确认后触发
Fantasy.util.jClass(Fantasy.awt.Box,{
	
	jClass : 'Fantasy.awt.ConfirmBox',
	
	initialize : function($super,settings){
		$super(settings);
		this.addEvents('confirm','cancel');
		
		var zhis = this;
        this.widget.find('.cancel').click(function(){
        	if(!(zhis.fireEvent('cancel',zhis,[zhis.widget,zhis.widget.data('trigger')])==false)){
        		zhis.widget.hide();
        	}
        });
        this.widget.find('.confirm').click(function(){
        	if(!(zhis.fireEvent('confirm',zhis,[zhis.widget,zhis.widget.data('trigger')])==false)){
        		zhis.widget.hide();
        	}
        });
	}
	
});