<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="key.title"/></title>
    <meta name="heading" content="<fmt:message key='key.userChange'/>"/>
    <meta name="menu" content="AdminMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<s:form name="keyForm" action="saveChangedUser" method="post" validate="true">
    <s:hidden name="appId"/>

    <c:set var="buttons">
        <s:submit key="button.save" method="changeUser" onclick="onFormSubmit(this.form)"/>
        <s:submit key="button.cancel" method="cancel"/>
    </c:set>


    <label for=”usKey”>User:</label>
    <select id="usKey" class="name" name="userKey">
        <option value="">--Select--</option>
        <c:forEach items="${userList}" var="user" varStatus="count">
            <option value='<c:out value="${user.id}" escapeXml="false"/>'>

                <c:out value="${user.username}" escapeXml="false"/>
            </option>
        </c:forEach>
    </select>


    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</s:form>
