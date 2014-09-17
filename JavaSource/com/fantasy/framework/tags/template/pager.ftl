<div id="${formId}-pages" class="pages megas512">
	<div class="pages_lf">
	<a href="<#if (page.currentPage lte 1) >#<#else>${page.currentPage-1}</#if>">«  上一页 </a><#--class="prev"-->
	<#assign pageIndexs = ObjectUtil.getPageArray(page,3)>
	<#list pageIndexs as index>
	<#if page.currentPage?string == index >
		<span class="current">${index}</span>
	<#elseif StringUtil.isNumber(index)>
		<a href="${index}">${index}</a>
	<#else>
		<span>${index}</span>
	</#if>
	</#list><#--class="next"-->
	<a href="<#if page.currentPage gte page.totalPage >#<#else>${page.currentPage+1}</#if>"> 下一页 »</a>
	</div>
	<div class="pages_turn">
		<span class="fr" style="margin-top:4px;*margin-top:0px !important;">
			<input class="pages_go page-go" type="button"/>
		</span>
		<span class="fr">共${page.totalCount}条记录&nbsp;转到  <input id="${formId}-page-go" type="text" class="pages_input"/> 页 </span>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$("a",$("#${formId}-pages")).click(function(){
			if($(this).attr("href")!='#'){
				$("input[name$=currentPage]",$("#${formId}")).val($(this).attr("href"));
				$('#${formId}').submit();
			}
			return false;
		});
		var rep = function(){
			$(this).val(this.value.replace(/\D/g,''));
			if($(this).val()!=''){
				$(this).val(Fantasy.getMax(Fantasy.toInt($(this).val()),1));
				$(this).val(Fantasy.getMin(Fantasy.toInt($(this).val()),${page.totalPage}));
			}
		};
		$("#${formId}-page-go").bind({keyup:rep,afterpaste:rep,keydown:function(e){
			if((e.keyCode == 40 || e.keyCode == 38) && $(this).val()==''){
				$(this).val(0);
			}
			if(e.keyCode == 40){
				$(this).val(Fantasy.getMax(Fantasy.toInt($(this).val())-1,1));
			}else if(e.keyCode == 38){
				$(this).val(Fantasy.getMin(Fantasy.toInt($(this).val())+1,${page.totalPage}));
			}else if(e.keyCode == 13){
				$("input[name$=currentPage]",$("#${formId}")).val($(this).val());
				$('#${formId}').submit();
			}
		}});
		$(".page-go",$("#${formId}-pages")).click(function(){
			if($("#${formId}-page-go").val()!=""){
				$("input[name$=currentPage]",$("#${formId}")).val($("#${formId}-page-go").val());
				$('#${formId}').submit();
			}
		});
		$('.sort',$('#${formId}')).click(function(){
			$('[name$=order]',$('#${formId}')).val($(this).hasClass('asc')?'desc':'asc');
			$('[name$=orderBy]',$('#${formId}')).val($(this).attr('orderBy'));
			$('#${formId}').submit();
		});
		//$("a[href=#]",$("#${formId}-pages")).hide();
	});
</script>