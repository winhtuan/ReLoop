<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <title>Dashboard</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded" />
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<style>
  main {
    background: #FFFFFF;
    height: 100%;
  }

  main .header {
    background: #F3F5F9;
    display: flex;
    align-items: center;
    /* C?n gi?a theo chi?u d?c */
    padding: 16px 24px;
    font-family: 'Poppins', sans-serif;
    gap: 12px;
    /* Kho?ng cách gi?a icon và ch? */
    border-bottom: 1px solid #ddd;
  }

  main .header h1 {
    font-size: 30px;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
  }

  main .header .material-symbols-rounded {
    font-size: 28px;
    color: #4a4a4a;
  }


  main .footer {
    background: #F3F5F9;
    height: 7vh;
  }

  main .row-1 {
    display: flex;
    justify-content: space-around;
    gap: 30px;
    flex-wrap: wrap;
    padding: 50px 20px;
    font-family: 'Inter', sans-serif;
  }

  .task {
    flex: 1 1 250px;
    display: flex;
    align-items: center;
    background: #ffffff;
    border-radius: 16px;
    padding: 30px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    transition: transform 0.2s ease;
  }

  .task:hover {
    transform: translateY(-5px);
  }

  .task-icon {
    background-color: #f0f4ff;
    color: #3f51b5;
    padding: 20px;
    border-radius: 50%;
    font-size: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20px;
  }

  .task-text h3 {
    font-size: 20px;
    font-weight: 600;
    margin: 0;
    color: #333;
  }

  .task-text p {
    font-size: 24px;
    font-weight: 700;
    margin: 5px 0 0;
    color: #3f51b5;
  }

  main .row-2 {
    display: flex;
    gap: 40px;
    flex-wrap: wrap;
    padding-top: 40px;
    justify-content: space-evenly;
    padding-bottom: 20px;
  }

  main .chart {
    background-color: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    width: 450px;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.2s ease;
  }

  main .chart:hover {
    transform: translateY(-5px);
  }

  main .chart-1 {
    background-color: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    width: 67%;
    height: 450px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.2s ease;
  }

  main .chart-1:hover {
    transform: translateY(-5px);
  }

  main .chart canvas {
    width: 100% !important;
    height: 100% !important;
  }
</style>

<body>
    <c:import url="/JSP/Admin/sidebar.jsp" />

  <main>
    <div class="header">
      <span class="material-symbols-rounded">dashboard</span>
      <h1>Dashboard</h1>
    </div>
    <div class="row-1">
      <div class="task">
        <div class="task-icon">
          <span class="material-symbols-rounded">person</span>
        </div>
        <div class="task-text">
          <h3>User</h3>
          <p>12</p>
        </div>
      </div>

      <div class="task">
        <div class="task-icon">
          <span class="material-symbols-rounded">article</span>
        </div>
        <div class="task-text">
          <h3>Post</h3>
          <p>12</p>
        </div>
      </div>

      <div class="task">
        <div class="task-icon">
          <span class="material-symbols-rounded">attach_money</span>
        </div>
        <div class="task-text">
          <h3>Revenue</h3>
          <p>$12K</p>
        </div>
      </div>

      <div class="task">
        <div class="task-icon">
          <span class="material-symbols-rounded">summarize</span>
        </div>
        <div class="task-text">
          <h3>Total Post</h3>
          <p>12</p>
        </div>
      </div>
    </div>

    <div class="row-2">
      <div class="chart-1">
        <canvas id="areaChart" width="900" height="400"></canvas>

        <script>
          const ctxArea = document.getElementById('areaChart').getContext('2d');

          const dataArea = {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
            datasets: [{
              label: 'L??t truy c?p',
              data: [30, 50, 40, 60, 70, 90],
              fill: true, // Bi?u ?? mi?n
              backgroundColor: 'rgba(75, 192, 192, 0.3)',
              borderColor: 'rgba(75, 192, 192, 1)',
              tension: 0.4
            }]
          };

          const configArea = {
            type: 'line',
            data: dataArea,
            options: {
              responsive: true,
              plugins: {
                title: {
                  display: true,
                  text: 'Bi?u ?? mi?n (Area Chart)'
                },
                legend: {
                  position: 'top'
                },
                tooltip: {
                  enabled: true,
                  intersect: false,
                  mode: 'index'
                }
              },
              scales: {
                y: {
                  beginAtZero: true
                }
              }
            }
          };

          new Chart(ctxArea, configArea);
        </script>


      </div>
      <div class="chart">
        <canvas id="myPieChart" width="400" height="400"></canvas>

        <script>
          const ctx = document.getElementById('myPieChart').getContext('2d');

          const data1 = {
            labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple'],
            datasets: [{
              label: 'T? l? ph?n tr?m',
              data: [30, 20, 15, 25, 10],
              backgroundColor: [
                'rgba(255, 99, 132, 0.6)',
                'rgba(54, 162, 235, 0.6)',
                'rgba(255, 206, 86, 0.6)',
                'rgba(75, 192, 192, 0.6)',
                'rgba(153, 102, 255, 0.6)'
              ],
              borderColor: 'white',
              borderWidth: 2
            }]
          };

          const config1 = {
            type: 'pie',
            data: data1,
            options: {
              responsive: true,
              plugins: {
                legend: {
                  position: 'bottom',
                },
                title: {
                  display: true,
                  text: 'Bi?u ?? hình tròn minh h?a d? li?u'
                }
              }
            },
          };

          new Chart(ctx, config1);
        </script>
      </div>
    </div>
    <div class="footer">

    </div>

  </main>

</body>

</html>