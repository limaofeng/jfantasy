/*
 * 更新地址：http://ajaxjs.com/docs
 * 欢迎参与我们翻译的工作！详见《EXT API2Chinese 相关事宜》：
 * http://bbs.ajaxjs.com/viewthread.php?tid=90 Ext中文站翻译小组
 * 
 * 本翻译采用“创作共同约定、Creative Commons”。您可以自由：
 * 复制、发行、展览、表演、放映、广播或通过信息网络传播本作品
 * 创作演绎作品
 * 请遵守：
 *    署名. 您必须按照作者或者许可人指定的方式对作品进行署名。
 * # 对任何再使用或者发行，您都必须向他人清楚地展示本作品使用的许可协议条款。
 * # 如果得到著作权人的许可，您可以不受任何这些条件的限制
 * http://creativecommons.org/licenses/by/2.5/cn/
 */
/**
 * @class Ext.data.Tree
 * @extends Ext.util.Observable
 * 该对象抽象了一棵树的结构和树节点的事件上报。树包含的节点拥有大部分的DOM功能。<br />
 * Represents a tree data structure and bubbles all the events for its nodes. The nodes
 * in the tree have most standard DOM functionality.
 * @constructor
 * @param {Node} root （可选的）根节点。(optional) The root node
 */
Ext.data.Tree = function(root){
   this.nodeHash = {};
   /**
    * 该树的根节点。The root node for this tree
    * @type Node
    * @property root
    */
   this.root = null;
   if(root){
       this.setRootNode(root);
   }
   this.addEvents(
       /**
        * @event append
        * 当有新的节点添加到这棵树的时候触发。
        * Fires when a new child node is appended to a node in this tree.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 新增加的节点。The newly appended node
        * @param {Number} index 新增加的节点的索引。The index of the newly appended node
        */
       "append",
       /**
        * @event remove
        * 当树中有子节点从一个节点那里移动开来的时候触发。
        * Fires when a child node is removed from a node in this tree.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 要移除的节点。The child node removed
        */
       "remove",
       /**
        * @event move
        * 当树中有节点被移动到新的地方时触发。
        * Fires when a node is moved to a new location in the tree
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} node 移动的节点 The node moved
        * @param {Node} oldParent 该节点的旧父节点。The old parent of this node
        * @param {Node} newParent 该节点的新父节点。The new parent of this node
        * @param {Number} index 被移动的原索引。The index it was moved to
        */
       "move",
       /**
        * @event insert
        * 当树中有一个新的节点添加到某个节点上的时候触发。
        * Fires when a new child node is inserted in a node in this tree.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 插入的子节点。The child node inserted
        * @param {Node} refNode 节点之前插入的子节点。The child node the node was inserted before
        */
       "insert",
       /**
        * @event beforeappend
        * 当树中有新的子节点添加到某个节点上之前触发，返回false表示取消这个添加行动。
        * Fires before a new child is appended to a node in this tree, return false to cancel the append.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 要被移除的子节点。The child node to be appended
        */
       "beforeappend",
       /**
        * @event beforeremove
        * 当树中有节点移动到新的地方之前触发，返回false表示取消这个移动行动。
        * Fires before a child is removed from a node in this tree, return false to cancel the remove.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 要被添加的子节点。The child node to be removed
        */
       "beforeremove",
       /**
        * @event beforemove
        * 当树中有新的子节点移除到某个节点上之前触发，返回false表示取消这个移除行动。
        * Fires before a node is moved to a new location in the tree. Return false to cancel the move.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} node 父节点 The node being moved
        * @param {Node} oldParent 节点的父节点。The parent of the node
        * @param {Node} newParent 要移动到的新的父节点。The new parent the node is moving to
        * @param {Number} index 要被移动到的索引。The index it is being moved to
        */
       "beforemove",
       /**
        * @event beforeinsert
        * 当树中有新的子节点插入某个节点之前触发，返回false表示取消这个插入行动。
        * Fires before a new child is inserted in a node in this tree, return false to cancel the insert.
        * @param {Tree} tree 主树。The owner tree
        * @param {Node} parent 父节点。The parent node
        * @param {Node} node 要被插入的子节点。The child node to be inserted
        * @param {Node} refNode 在插入之前节点所在的那个子节点。The child node the node is being inserted before
        */
       "beforeinsert"
   );

    Ext.data.Tree.superclass.constructor.call(this);
};

Ext.extend(Ext.data.Tree, Ext.util.Observable, {
    /**
     * @cfg {String} pathSeparator 分割节点Id的标识符（默认为"/"）。
     * The token used to separate paths in node ids (defaults to '/').
     */
    pathSeparator: "/",

    // private
    proxyNodeEvent : function(){
        return this.fireEvent.apply(this, arguments);
    },

    /**
     * 为这棵树返回根节点。
     * Returns the root node for this tree.
     * @return {Node}
     */
    getRootNode : function(){
        return this.root;
    },

    /**
     * 设置这棵树的根节点。
     * Sets the root node for this tree.
     * @param {Node} node 节点
     * @return {Node}
     */
    setRootNode : function(node){
        this.root = node;
        node.ownerTree = this;
        node.isRoot = true;
        this.registerNode(node);
        return node;
    },

    /**
     * 根据ID查找节点。
     * Gets a node in this tree by its id.
     * @param {String} id 
     * @return {Node}
     */
    getNodeById : function(id){
        return this.nodeHash[id];
    },

    // private
    registerNode : function(node){
        this.nodeHash[node.id] = node;
    },

    // private
    unregisterNode : function(node){
        delete this.nodeHash[node.id];
    },

    toString : function(){
        return "[Tree"+(this.id?" "+this.id:"")+"]";
    }
});
