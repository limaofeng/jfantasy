Fantasy.util.jClass(Fantasy.util.Observable,(function(){
	
	var bubble_fun_chk = function(){
		if (!this.element || !this.isExpandable())return;
		var chk = this.$html.children('.chk');
		if(chk.length > 0){
			var classname = chk.attr('class').split(' ')[1];			
	        var tnum = 0, pnum = 0 ,size = 0;
	        this.childNodes.each(function(){
				var _classname = this.$html.children('.chk').attr('class').split(' ')[1];	
	        	if(/true_part/.test(_classname)){
	         		pnum++;
	        	} else if(/true_full/.test(_classname)){
	        		tnum++;
	       		}
				size++;
	        });
			if (tnum == size) {
				chk.removeClass(classname).addClass('checkbox_true_full');
				this.attr('checked',true);
			}else if(tnum == 0 && pnum == 0){
				chk.removeClass(classname).addClass('checkbox_false_full');
				this.attr('checked',false);
			}else{
				chk.removeClass(classname).addClass('checkbox_true_part');
				this.attr('checked',false);
			}
		}
	};
	
	return {
	
		jClass: 'Fantasy.awt.Node',
		
		initialize: function($super, data, config, tree){
			$super();
			this.addEvents('insert');
			
			var zthis = this;
			this.setPropertyValue({}, {
				leaf:true,
				element:'',
				$html:$(''),
				tree: tree,
				data: (function(data){
					data = data ? data : {};
					data['display'] = data['display'] ? true : false;
					data['expandable'] = data['expandable'] ? true : false;
					data['checked'] = data['checked'] ? true : false;
					data['items'] = (!data[tree.itemsName] || data[tree.itemsName].length == 0) ? null : data[tree.itemsName];
					return data;
				})(data),
				view: new Fantasy.awt.View(config.tree,{
					template: config.node,
					sortable:{
						//distance:30,
						revert: true,
						templateClass:'.node',
						cancel:'.head',
						//axis:'y',	
						helper: 'clone',		
						update:function(event, ui){
						
						}
					}
				}),
				childNodes: new Fantasy.util.Collection(),
				parentNode:null
			});
			
			this.on('insert', function(){
				var node = this;
				var element = this.element;
				var li = this.$html;
				
				if (element.getValue('items')) {
					this.leaf = false;
					li.children('button[class^=switch_]').click(function(){
						var isShow = !/open$/.test($(this).attr('class'));
						element.setValue('display', isShow);
						li.data('node').switchClass(null, isShow ? 'open' : 'close');
						isShow ? li.children('ul').addClass('line').show().css('display','block') : li.children('ul').removeClass('line').hide();
						zthis.tree.fireEvent('handleList', node, [node.tree, node.parentNode, node]);
						if(li.children('button[class^=switch_bottom_]:first').length > 0){
							li.children('ul').removeClass('line');
						}else{
							li.children('ul').addClass('line');
							//li.children('ul').css('background','url("tree.images/line_conn.gif") repeat-y scroll 0 0 transparent;');
						}
					});
					if (this.attr('checked')) {
						this.setJSON(this.attr('items'));
						node.cascade(function(){
							var chk = this.$html.children('.chk');
							if (chk.length > 0) {
								var classname = chk.attr('class').split(' ')[1];
								chk.removeClass(classname).addClass('checkbox_true_full');
								this.attr('checked', true);
							}
						});
						node.bubble(bubble_fun_chk);
					} else {
						li.data('node').setJSON(this.attr('items'));
						li.data('node').bubble(bubble_fun_chk);
					}
					if (this.attr('display'))this.display();
				} else {
					li.data('node').switchClass(null, 'docu');					
					if(this.attr('checked')){
                        var chk = this.$html.children('.chk');
						if(chk.length > 0){
	                        var classname = chk.attr('class').split(' ')[1];
	                        chk.removeClass(classname).addClass('checkbox_true_full');
							this.bubble(bubble_fun_chk);
						}
					}
				}
				zthis.tree.fireEvent('insert', this, [zthis.tree, this.parentNode, this]);
			});
			
			this.on('insert', function(){
				var node = this;
				var element = this.element;
				var li = this.$html;
				var parent = this.parentNode;
				
				var chk = li.children('.chk');
				if (chk.length == 0) 
					return;
				
				chk.hover(function(){//checkbox 鼠标悬停事件
					var classname = $(this).attr('class').split(' ')[1];
					if (!/_focus$/.test(classname)) 
						$(this).removeClass(classname).addClass(classname + '_focus');
				}, function(){
					var classname = $(this).attr('class').split(' ')[1];
					$(this).removeClass(classname).addClass(classname.replace(/^(checkbox_(true|false)_(full|part))_focus$/, '$1'));
				});
				
				chk.click(function(){
					var classname = chk.attr('class').split(' ')[1];
					if (/false_full/.test(classname)) {
						node.cascade(function(){
							var chk = this.$html.children('.chk');
							var classname = chk.attr('class').split(' ')[1];
							chk.removeClass(classname).addClass('checkbox_true_full');
							this.attr('checked', true);
						});
					} else {
						node.cascade(function(){
							var chk = this.$html.children('.chk');
							var classname = chk.attr('class').split(' ')[1];
							chk.removeClass(classname).addClass('checkbox_false_full');
							this.attr('checked', false);
						});
					}
					node.bubble(bubble_fun_chk);
				});
			});
			
			this.childNodes.on('add', function(node, index){
				node.parentNode = zthis;//设置父节点
				node.element = zthis.view.add(node.data);//向html添加元素
				node.$html = node.element.target.data('node', node);
				node.$html.children('ul').replaceWith(node.view.target);
				node.$html.data('node', node);				
				node.$html.children('ul').removeClass('tree');
			});
			this.childNodes.on('clear', function(){
				zthis.view.clear();
			});
			this.childNodes.on('remove', function(obj,seq){
				zthis.view.remove(seq);
			});
		},
		
		display: function(){
			this.bubble(function(){
				if (!this.element || !this.isExpandable())return;							
				this.attr('display', true);
				this.switchClass(null,'open');
				this.$html.children('ul').addClass('line').show().css('display','block');
//				this.tree.fireEvent('handleList', this, [this.tree, this.parentNode, this]);
			});
		},
		
		hidden: function(){
			this.bubble(function(){
				if (!this.element || !this.isExpandable())return;							
				this.attr('display', false);
				this.switchClass(null,'close');
				this.$html.children('ul').addClass('line').hide().css('display','none');
//				this.tree.fireEvent('handleList', this, [this.tree, this.parentNode, this]);
			});
		},
		
		setJSON: function(json){
			if(!json)return;
			this.childNodes.clear();
			for (var i = 0, len = json.length; i < len; i++) {
				var $n = this.view.getTemplate().template.clone();
				this.appendChild(new Fantasy.awt.Node(json[i], {
					tree: $n.children('ul'),
					node: $n
				}, this.tree));
			}
		},
		
		attr:function(name,value){
			if(!this.element)return;
			if(value == undefined){
				return this.element.getValue(name);
			}else{
				this.element.setValue(name,value);
			}
		},
		
		/**
		 * 在该节点里面最后的位置上插入节点，可以多个。
		 */
		appendChild: function(node){
			var multi = false;
			if (Fantasy.isArray(node)) {
				multi = node;
			}
			else 
				if (arguments.length > 1) {
					multi = arguments;
				}
				else 
					if (node != null) {
						multi = [node];
					}
			for (var i = 0, len = multi.length; i < len; i++) {
				var n = multi[i];
				if (n.toString() == '[object Fantasy.awt.Node]') {
					this.childNodes.add(n);
					this.setLastChild(n);
					this.fireEvent('insert', node, [])
				}
			}
			
		},

		/**
		 * 从父节点上移除子节点。
		 * @param {Object} seq
		 */
		remove: function(seq){
			this.childNodes.remove(seq);
			return this;
		},
		
		/**
		 * 若节点是叶子节点的话返回true。
		 * @return {Boolean}
		 */
		isLeaf: function(){
			return this.leaf === true;
		},
		
		// private
		setFirstChild: function(node){
			this.firstChild = node;
		},
		
		//private
		setLastChild: function(node){
			if (this.lastChild) 
				this.lastChild.switchClass('center');
			this.lastChild = node;
			this.lastChild.switchClass('bottom');
		},
		
		switchClass: function(w, c){
			var li = this.$html;			
			var classname = li.children('button[class^=switch_]').attr('class');
			if (w) {
				classname = classname.replace(/^(switch_)(roots|center|bottom)(_(close|open|docu))$/, '$1' + w + '$3');
			}
			if (c) {
				classname = classname.replace(/^(switch_(roots|center|bottom)_)(close|open)$/, '$1' + c);
			}
			li.children('button[class^=switch_]').attr('class', classname);
		},
		
		getLastChild: function(node){
			return this.lastChild;
		},
		
		/**
		 * 如果有子节点返回true。
		 * @return {Boolean}
		 */
		hasChildNodes: function(){
			return !this.isLeaf() && this.childNodes.size() > 0;
		},
		
		/**
		 * 如果该节点有一个或多个的子节点，或其节点<tt>可展开的</tt>属性是明确制定为true的话返回true。
		 * @return {Boolean}
		 */
		isExpandable: function(){
			return (this.element && this.element.getValue('expandable')) || this.hasChildNodes();
		},
		/**
		 * 如果这个节点是其父节点下面的最后一个节点的话返回true。
		 * Returns true if this node is the last child of its parent
		 * @return {Boolean}
		 */
		isLast: function(){
			return (!this.parentNode ? true : this.parentNode.lastChild == this);
		},
		
		/**
		 * 如果这个节点是其父节点下面的第一个节点的话返回true。
		 * Returns true if this node is the first child of its parent
		 * @return {Boolean}
		 */
		isFirst: function(){
			return (!this.parentNode ? true : this.parentNode.firstChild == this);
		},
		
		/**
		 * 从该节点中移除一个子节点。
		 * Removes a child node from this node.
		 * @param {Node} node 要移除的节点。The node to remove
		 * @return {Node} 移除后的节点。The removed node
		 */
		removeChild: function(node){
			var node = this.remove(this.getElements().indexOf(node.getNode()));
			this.fireEvent("remove", this.ownerTree, this, node);
			return node;
		},
		
		/**
		 * 在当前节点的子节点的集合中，位于第二个节点之前插入节点（第一个参数）。
		 * Inserts the first node before the second node in this nodes childNodes collection.
		 * @param {Node} node 要插入的节点。he node to insert
		 * @param {Node} refNode 前面插入的节点（如果为null表示节点是追加的）。The node to insert before (if null the node is appended)
		 * @return {Node} 插入的节点。The inserted node
		 */
		insertBefore: function(node, refNode){
			if (!refNode) { // like standard Dom, refNode can be null for append
				return this.appendChild(node);
			}
			if (node == refNode) {
				return false;
			}
			if (this.fireEvent("beforeinsert", this.ownerTree, this, node, refNode) === false) {
				return false;
			}
			var index = this.childNodes.indexOf(refNode);
			var oldParent = node.parentNode;
			var refIndex = index;
			if (oldParent == this && this.childNodes.indexOf(node) < index) {
				refIndex--;
			}
			if (oldParent) {
				if (node.fireEvent("beforemove", node.getOwnerTree(), node, oldParent, this, index, refNode) === false) {
					return false;
				}
				oldParent.removeChild(node);
			}
			if (refIndex == 0) {
				this.setFirstChild(node);
			}
			this.childNodes.splice(refIndex, 0, node);
			node.parentNode = this;
			var ps = this.childNodes[refIndex - 1];
			if (ps) {
				node.previousSibling = ps;
				ps.nextSibling = node;
			}
			else {
				node.previousSibling = null;
			}
			node.nextSibling = refNode;
			refNode.previousSibling = node;
			node.setOwnerTree(this.getOwnerTree());
			this.fireEvent("insert", this.ownerTree, this, node, refNode);
			if (oldParent) {
				node.fireEvent("move", this.ownerTree, node, oldParent, this, refIndex, refNode);
			}
			return node;
		},
		
		/**
		 * 指定索引，在自节点中查找匹配索引的节点。
		 * @param {Number} index
		 * @return {Node}
		 */
		item: function(index){
			return this.childNodes.get(index);
		},
		
		/**
		 * 把下面的某个子节点替换为其他节点。
		 * @param {Node} newChild 进来的节点。The replacement node
		 * @param {Node} oldChild 去掉的节点。The node to replace
		 * @return {Node} 去掉的节点。The replaced node
		 */
		replaceChild: function(newChild, oldChild){
			var s = oldChild ? oldChild.nextSibling : null;
			this.removeChild(oldChild);
			this.insertBefore(newChild, s);
			return oldChild;
		},
		
		/**
		 * 返回某个子节点的索引。
		 * @param {Node} node 节点
		 * @return {Number} 节点的索引或是-1表示找不到。The index of the node or -1 if it was not found
		 */
		indexOf: function(child){
			return this.childNodes.indexOf(child);
		},
		
		/**
		 * 返回节点所在的树对象。
		 * Returns the tree this node is in.
		 * @return {Tree}
		 */
		getOwnerTree: function(){
			if (!this.ownerTree) {
				var p = this;
				while (p) {
					if (p.ownerTree) {
						this.ownerTree = p.ownerTree;
						break;
					}
					p = p.parentNode;
				}
			}
			return this.ownerTree;
		},
		
		/**
		 * 返回该节点的深度（根节点的深度是0）。
		 * @return {Number}
		 */
		getDepth: function(){
			var depth = 0;
			var p = this;
			while (p.parentNode) {
				++depth;
				p = p.parentNode;
			}
			return depth;
		},
		
		setOwnerTree: function(tree){
			if (tree != this.ownerTree) {
				if (this.ownerTree) {
					this.ownerTree.unregisterNode(this);
				}
				this.ownerTree = tree;
				var cs = this.childNodes;
				for (var i = 0, len = cs.length; i < len; i++) {
					cs[i].setOwnerTree(tree);
				}
				if (tree) {
					tree.registerNode(this);
				}
			}
		},
		
		/**
		 * 改变该节点的id
		 * @param {String} id 节点的新id。The new id for the node.
		 */
		setId: function(id){
			if (id !== this.id) {
				var t = this.ownerTree;
				if (t) {
					t.unregisterNode(this);
				}
				this.id = id;
				if (t) {
					t.registerNode(this);
				}
				this.onIdChange(id);
			}
		},
		
		/**
		 * 返回该节点的路径，以方便控制这个节点展开或选择。
		 * Returns the path for this node. The path can be used to expand or select this node programmatically.
		 * @param {String} attr（可选的）路径采用的属性（默认为节点的id）。 (optional) The attr to use for the path (defaults to the node's id)
		 * @return {String} 路径。The path
		 */
		getPath: function(attr){
			attr = attr || "id";
			var p = this.parentNode;
			var b = [this.attributes[attr]];
			while (p) {
				b.unshift(p.attributes[attr]);
				p = p.parentNode;
			}
			var sep = this.getOwnerTree().pathSeparator;
			return sep + b.join(sep);
		},
		
		/**
		 * 从该节点开始逐层上报(Bubbles up)节点，上报过程中对每个节点执行指定的函数。
		 * 函数的作用域（<i>this</i>）既可是参数scope传入或是当前节点。
		 * 函数的参数可经由args指定或当前节点，如果函数在某一个层次上返回false，上升将会停止。
		 * @param {Function} fn 要调用的函数。
		 * @param {Object} scope（可选的）函数的作用域（默认为当前节点）。
		 * @param {Array} args （可选的）调用的函数要送入的参数（不指定就默认为遍历过程中当前的节点）。
		 */
		bubble: function(fn, scope, args){
			var p = this;
			while (p) {
				if (fn.apply(scope || p, args || [p]) === false) {
					break;
				}
				p = p.parentNode;
			}
		},
		
		/**
		 * 从该节点开始逐层下报(Bubbles up)节点，上报过程中对每个节点执行指定的函数。
		 * 函数的作用域（<i>this</i>）既可是参数scope传入或是当前节点。
		 * 函数的参数可经由args指定或当前节点，如果函数在某一个层次上返回false，下升到那个分支的位置将会停止。
		 * @param {Function} fn 要调用的函数。The function to call
		 * @param {Object} scope （可选的） 函数的作用域（默认为当前节点）。 (optional) The scope of the function (defaults to current node)
		 * @param {Array} args （可选的） 调用的函数要送入的参数（不指定就默认为遍历过程中当前的节点）。 (optional) The args to call the function with (default to passing the current node)
		 */
		cascade: function(fn, scope, args){
			if (fn.apply(scope || this, args || [this]) !== false) {
				var cs = this.childNodes;
				for (var i = 0, len = cs.size(); i < len; i++) {
					cs.get(i).cascade(fn, scope, args);
				}
			}
		},
		
		/**
		 * 遍历该节点下的子节点，枚举过程中对每个节点执行指定的函数。函数的作用域（<i>this</i>）既可是参数scope传入或是当前节点。
		 * 函数的参数可经由args指定或当前节点，如果函数走到某处地方返回false，遍历将会停止。
		 * @param {Function} fn 要调用的函数。The function to call
		 * @param {Object} scope（可选的） 函数的作用域（默认为当前节点）。(optional)The scope of the function (defaults to current node)
		 * @param {Array} args （可选的） 调用的函数要送入的参数（不指定就默认为遍历过程中当前的节点）。(optional)The args to call the function with (default to passing the current node)
		 */
		eachChild: function(fn, scope, args){
			var cs = this.childNodes;
			for (var i = 0, len = cs.size(); i < len; i++) {
				if (fn.apply(scope || this, args || [cs.get(i)]) === false) {
					break;
				}
			}
		},
		
		/**
		 * 根据送入的值，如果子节点身上指定属性的值是送入的值，就返回那个节点。
		 * @param {String} attribute 属性名称。The attribute name
		 * @param {Mixed} value 要查找的值。The value to search for
		 * @return {Node} 已找到的子节点或null表示找不到。The found child or null if none was found
		 */
		findChild: function(attribute, value){
			var cs = this.childNodes;
			for (var i = 0, len = cs.length; i < len; i++) {
				if (cs[i].attributes[attribute] == value) {
					return cs[i];
				}
			}
			return null;
		},
		
		/**
		 * 通过自定义的函数查找子节点，找到第一个合适的就返回。要求的条件是函数返回true。
		 * Finds the first child by a custom function. The child matches if the function passed returns true.
		 * @param {Function} fn 函数
		 * @param {Object} scope 函数作用域（可选的）。(optional)
		 * @return {Node} 找到的子元素或null就代表没找到。The found child or null if none was found
		 */
		findChildBy: function(fn, scope){
			var cs = this.childNodes;
			for (var i = 0, len = cs.length; i < len; i++) {
				if (fn.call(scope || cs[i], cs[i]) === true) {
					return cs[i];
				}
			}
			return null;
		},
		
		/**
		 *  用自定义的排序函数对节点的子函数进行排序。
		 *  Sorts this nodes children using the supplied sort function
		 * @param {Function} fn 函数
		 * @param {Object} scope 函数（可选的）。(optional)
		 */
		sort: function(fn, scope){
			var cs = this.childNodes;
			var len = cs.length;
			if (len > 0) {
				var sortFn = scope ? function(){
					fn.apply(scope, arguments);
				} : fn;
				cs.sort(sortFn);
				for (var i = 0; i < len; i++) {
					var n = cs[i];
					n.previousSibling = cs[i - 1];
					n.nextSibling = cs[i + 1];
					if (i == 0) {
						this.setFirstChild(n);
					}
					if (i == len - 1) {
						this.setLastChild(n);
					}
				}
			}
		},
		
		/**
		 * 返回true表示为该节点是送入节点的祖先节点（无论在哪那一级的）。
		 * Returns true if this node is an ancestor (at any point) of the passed node.
		 * @param {Node} node 节点
		 * @return {Boolean}
		 */
		contains: function(node){
			return node.isAncestor(this);
		},
		
		/**
		 * 返回true表示为送入的节点是该的祖先节点（无论在哪那一级的）。
		 * Returns true if the passed node is an ancestor (at any point) of this node.
		 * @param {Node} node 节点
		 * @return {Boolean}
		 */
		isAncestor: function(node){
			var p = this.parentNode;
			while (p) {
				if (p == node) {
					return true;
				}
				p = p.parentNode;
			}
			return false;
		}
	};
	
})());