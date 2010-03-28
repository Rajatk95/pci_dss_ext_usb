<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="key.title"/></title>
    <meta name="heading" content="<fmt:message key='key.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<s:form name="keyForm" action="saveRegenerateKey" method="post" >
<li style="display: none">
    <s:hidden name="key.app_id"/>
    <s:hidden name="key.name"/>
</li>    

    <c:set var="buttons">
        <s:submit key="button.regenerate" method="regenerateKey" />
        <s:submit key="button.cancel" method="cancel"/>
    </c:set>
    <li class="info">
	    <label style='font-weight: bold;'>Application Name:<c:out value="${key.name}" escapeXml="false"/></span></label>
    </li>    
    
    <s:password key="key.oldPassword" showPassword="true" theme="xhtml" required="true"
                    cssClass="text large" />

    <li>
        <div>
            <div class="left">
                <s:password key="key.newPassword" showPassword="true" theme="xhtml" required="true"
                            cssClass="text medium" />
            </div>
            <div>
                <s:password key="key.confirmPassword" theme="xhtml" required="true"
                            showPassword="true" cssClass="text medium" />
            </div>
        </div>
    </li>
	
    <li>
    <div>
    <div class="left">
    <label style='font-weight: bold; ' for=usKey>Group:<span class="req">*</span></label>
    </div>
    <div>
    <select id="usKey" cssClass="text large" name="key.group_id">
        <option value="">--Select--</option>
        <c:forEach items="${groupList}" var="group" varStatus="count">
            <c:choose>
                <c:when test="${key.group_id == group.groupId}">
                    <option value='<c:out value="${group.groupId}" escapeXml="false"/>' selected="true">
                        <c:out value="${group.groupName}" escapeXml="false"/>
                    </option>
                </c:when>
                <c:otherwise>
                    <option value='<c:out value="${group.groupId}" escapeXml="false"/>'>
                        <c:out value="${group.groupName}" escapeXml="false"/>
                    </option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    </div>
    </div>
    </li>


    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</s:form>
