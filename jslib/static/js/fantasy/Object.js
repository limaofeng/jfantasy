Fantasy.apply(Object, {}, {

	clone : function(zhis) {
		var objClone;
		if (zhis.constructor == Object) {
			objClone = new zhis.constructor();
		} else {
			objClone = new zhis.constructor(zhis.valueOf());
		}
		for ( var key in zhis) {
			if (objClone[key] != zhis[key]) {
				if (typeof (zhis[key]) == 'object') {
					objClone[key] = Object.clone(zhis[key]);
				} else {
					objClone[key] = zhis[key];
				}
			}
		}
		objClone.toString = zhis.toString;
		objClone.valueOf = zhis.valueOf;
		return objClone;
	}

});