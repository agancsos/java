<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
	<head>
		<title>CacheMoney | Transactions</title>
		<link rel="stylesheet" href="/main.css"/>
	</head>
	<body>
		<div id="banner">
			<div id="banner-inner">
				<div class="logo">CacheMoney</div>
			</div>
		</div>
		<div id="links">
			<div id="links-inner">
			</div>
		</div>
		<div id="main">
			<div id="main-inner">
				<c:choose>
    				<c:when test="${fn:length(results) < 1}">
						No transactions to display... <br/>
    				</c:when>    
    				<c:otherwise>
						<c:set var="balance" value="0.00"/>
						<c:forEach items="${results}" var="transaction">
                            <c:choose>
                                <c:when test="${transaction.direction > 0}">
									<c:set var="balance" value="${balance + transaction.amount}"/>
                                </c:when>
                                <c:otherwise>
									<c:set var="balance" value="${balance - transaction.amount}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
						<table id="balance-table">
							<tr>
								<th>Balance</th>
								<td>$${balance}</td>
							</tr>
						</table>
						<br/>
						<table id="transaction-table">
                        <tr>
                        	<th>TransactionID</th>
                        	<th>Method</th>
                        	<th>Amount</th>
							<th>Balance</th>
                        	<th>Date</th>
                        </tr>
						<c:forEach items="${results}" var="transaction">
							<tr>
								<td><a href="./transaction?trans=${transaction.transactionId}">${transaction.transactionId}</a></td>
								<td>${transaction.method}</td>
								<c:choose>
									<c:when test="${transaction.direction > 0}">
										<td style="background-color:darkgreen;color:white;">$${transaction.amount}</td>
									</c:when>
									<c:otherwise>
										<td style="background-color:darkred;color:white;">$${transaction.amount}</td>
									</c:otherwise>
								</c:choose>
								<td>$${transaction.balance}</td>
								<td>${transaction.lastUpdatedDate}</td>
							</tr>
						</c:forEach>
                        </table>	
    				</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div id="footer">
			<div id="footer-inner">
				&copy; <%= (new Date()).getYear() + 1900 %> Abel Gancsos <br/>
				All Rights Reserved
			</div>
		</div>
	</body>
</html>
