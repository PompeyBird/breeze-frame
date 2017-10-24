/**
 * 弹出框提示
 */
function showNoty(text, layout, type, modal, timeout, closFunc){
	var options = {
			text: text,
			type: type,
			layout: layout,
			modal: modal
	};
	if(timeout==null){
		timeout = 3000;
	}
	options.timeout = timeout;
	if(null != closFunc){
		options.onClosed = closFunc;
	}
	return noty(options);
}