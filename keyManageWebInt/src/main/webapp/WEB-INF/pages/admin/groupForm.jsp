<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="groupForm.title"/></title>
    <meta name="heading" content="<fmt:message key='groupForm.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<s:form name="groupForm" action="saveGroup" method="post" validate="true">
    <c:set var="buttons">
        <s:submit key="button.save" method="save" onclick="onFormSubmit(this.form)"/>
        <s:submit key="button.cancel" method="cancel"/>
    </c:set>
    <s:hidden name="group.groupId"/>


    <s:textfield key="group.groupName" cssClass="text large" required="true"/>

    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</s:form>
