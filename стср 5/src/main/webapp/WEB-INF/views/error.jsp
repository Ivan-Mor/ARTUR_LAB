<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>IT-отдел</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body><main><h1>Не удалось выполнить запрос</h1><p class="error"><c:out value="${error}"/></p><p><a href="${pageContext.request.contextPath}/controller?action=projects">К списку проектов</a></p></main></body></html>
