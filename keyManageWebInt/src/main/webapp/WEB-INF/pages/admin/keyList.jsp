<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="keyList.title"/></title>
    <meta name="heading" content="<fmt:message key='keyList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
           onclick="location.href='<c:url value="/admin/createNewKey.html?method=Add&from=list"/>'"
           value="<fmt:message key="button.createNewKey"/>"/>
<%--
    <input type="button" onclick="location.href='<c:url value="/admin/regenerateAllKeys.html?from=list"/>'"
           value="<fmt:message key="button.regenerateAllKeys"/>"/>
    <c:if test="${empty keyList}">
        <input type="button" onclick="location.href='<c:url value="/admin/generateNewKeys.html?from=list"/>'"
               value="<fmt:message key="button.generateNewKeys"/>"/>
    </c:if>
--%>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="keyList" cellspacing="0" cellpadding="0" requestURI=""
               defaultsort="1" id="key" pagesize="25" class="table" export="false">
    <display:column property="name" url="/admin/editKey.html?from=list" paramId="id" paramProperty="app_id"
                    escapeXml="true" sortable="true" titleKey="key.appid" style="width: 25%"/>
    <display:column property="certificate_id" escapeXml="true" sortable="true" titleKey="key.certificate"
                    style="width: 25%"></display:column>
    <display:column property="groupname" escapeXml="true" sortable="true" titleKey="key.groupname" style="width: 25%"/>
    <display:column titleKey="key.action" value="regenerate" url="/admin/regenerateKey.html?from=list" paramId="id"
                    paramProperty="app_id" escapeXml="true" sortable="false"/>
    <%-- removed this functionality as it is being replaced with digital signature based authentication --%>
    <%--
    <display:column titleKey="key.action" value="changeUser" url="/admin/changeUser.html?from=list" paramId="app_id"
                    paramProperty="app_id" escapeXml="true" sortable="false"/>
    --%>
    <display:column titleKey="key.action" value="delete" url="/admin/deleteKey.html?from=list" paramId="id"
                    paramProperty="name" escapeXml="true" sortable="false"/>


    <display:setProperty name="paging.banner.item_name" value="key"/>
    <display:setProperty name="paging.banner.items_name" value="keyList"/>

</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("keyList");
</script>
