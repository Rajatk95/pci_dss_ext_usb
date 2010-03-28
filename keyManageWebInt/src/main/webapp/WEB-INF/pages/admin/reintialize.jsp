<%--
  User: s726580
  Date: 24-Feb-2009
  Time: 10:59:38
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="reintialize.title"/></title>
    <meta name="heading" content="<fmt:message key='reintialize.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<div class="message">
    <img src="<c:url value="/images/iconInformation.gif"/>"
         alt="<fmt:message key="icon.information"/>" class="icon"/>
    <fmt:message key="reintialize.message"/>
</div>
<script type="text/javascript">
    window.setTimeout("history.back()", 2000);
</script>