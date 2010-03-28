<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="group.title"/></title>
    <meta name="heading" content="<fmt:message key='group.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
           onclick="location.href='<c:url value="/admin/createNewGroup.html?method=Add&from=list"/>'"
           value="<fmt:message key="button.createNewGroup"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="groupList" cellspacing="0" cellpadding="0" requestURI=""
               defaultsort="1" id="group" pagesize="25" class="table" export="false">
    <display:column property="groupId" escapeXml="true" sortable="true" titleKey="group.groupId" style="width: 25%"/>
    <display:column property="groupName" escapeXml="true" sortable="true" titleKey="group.groupName" paramId="id"
                    url="/admin/editGroup.html?method=edit&from=list"
                    paramProperty="groupId" style="width: 25%"/>
    <display:column titleKey="group.action" value="delete" url="/admin/deleteGroup.html?from=list" paramId="id"
                    paramProperty="groupId" escapeXml="true" sortable="false"/>


    <display:setProperty name="paging.banner.item_name" value="group"/>
    <display:setProperty name="paging.banner.items_name" value="groupList"/>

</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("keyList");
</script>
