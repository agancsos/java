<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
	<head>
		<title>CacheMoney | Transaction</title>
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
    				<c:when test="${transaction.transactionId == -1}">
						Invalid transaction id... <br/>
    				</c:when>    
    				<c:otherwise>
						<table id="transaction-table">
                       	<tr><th>TransactionID</th><td>${transaction.transactionId}</td></tr>
                       	<tr><th>Method</th><td>${transaction.method}</td></tr>
						<tr><th>Direction</th><td>
						<c:choose>
							<c:when test="${transaction.direction} > 0}">
								Deposit
							</c:when>
							<c:otherwise>
								Withdrawal
							</c:otherwise>
						</c:choose>
						</td></tr>
                       	<tr><th>Amount</th><td>$${transaction.amount}</td></tr>
						<tr><th>Balance</th><td>$${transaction.balance}</td></tr>
                       	<tr><th>Date</th><td>${transaction.lastUpdatedDate}</td></tr>
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
