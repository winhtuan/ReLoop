<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Utils.AppConfig" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign Up</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            body {
                background: linear-gradient(135deg, #e0eafc, #cfdef3);
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                margin: 0;
                font-family: 'Inter', sans-serif;
                position: relative;
                overflow: hidden;
            }

            .signup-card {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 2rem;
                width: 100%;
                max-width: 900px;
                box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
                position: relative;
                z-index: 10;
            }

            .signin-button {
                position: fixed;
                top: 1rem;
                right: 1rem;
                background: #4c51bf;
                color: white;
                padding: 0.5rem 1.5rem;
                border-radius: 20px;
                font-weight: 500;
                font-size: 1rem;
                transition: all 0.3s ease;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                z-index: 20;
            }

            .signin-button:hover {
                background: #3b82f6;
                transform: translateY(-2px);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
            }

            .input-field {
                width: 100%;
                padding: 0.75rem;
                margin-bottom: 1rem;
                border: 1px solid #e5e7eb;
                border-radius: 10px;
                font-size: 1rem;
                transition: all 0.3s ease;
            }

            .input-field:focus {
                border-color: #4c51bf;
                outline: none;
                box-shadow: 0 0 0 3px rgba(76, 81, 191, 0.2);
            }

            .submit-button {
                width: 100%;
                padding: 0.75rem;
                background: #4c51bf;
                color: white;
                border-radius: 20px;
                font-size: 1.1rem;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            .submit-button:hover {
                background: #3b82f6;
                transform: translateY(-2px);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
            }

            .social-button {
                width: 100%;
                padding: 0.75rem;
                border: 1px solid #e5e7eb;
                border-radius: 10px;
                font-size: 1rem;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                transition: all 0.3s ease;
            }

            .social-button:hover {
                background: #f9fafb;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            }

            .divider {
                text-align: center;
                margin: 1.5rem 0;
                color: #6b7280;
                font-size: 0.875rem;
            }

            .error-message {
                background: #fef2f2;
                border: 1px solid #fecaca;
                color: #dc2626;
                padding: 0.75rem;
                border-radius: 10px;
                text-align: center;
                margin-bottom: 1rem;
            }
        </style>
    </head>
    <body>
        <a href="login.jsp" class="signin-button">Sign In</a>
        <div class="signup-card">
            <section class="text-center">
                <div class="mb-5">
                    <h2 class="text-3xl font-bold text-gray-800">Sign Up</h2>
                </div>
                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>
                <form action="RegisterServlet" method="POST" class="space-y-6">
                    <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
                        <div>
                            <label for="firstName" class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                            <input type="text" name="firstName" id="firstName" class="input-field" placeholder="First Name" required>
                        </div>
                        <div>
                            <label for="lastName" class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                            <input type="text" name="lastName" id="lastName" class="input-field" placeholder="Last Name" required>
                        </div>
                        <div>
                            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                            <input type="email" name="email" id="email" class="input-field" placeholder="name@example.com" required>
                        </div>
                        <div>
                            <label for="password" class="block text-sm font-medium text-gray-700 mb-1">Password</label>
                            <input type="password" name="password" id="password" class="input-field" placeholder="Password" required>
                        </div>
                        <div>
                            <label for="address" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
                            <input type="text" name="address" id="address" class="input-field" placeholder="Address" required>
                        </div>
                        <div>
                            <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                            <input type="tel" name="phone" id="phone" class="input-field" placeholder="Phone Number" required>
                        </div>
                    </div>
                    <div class="flex items-center space-x-2">
                        <input type="checkbox" name="iAgree" id="iAgree" class="h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500" required>
                        <label for="iAgree" class="text-sm text-gray-600">I agree to the <a href="#!" class="text-blue-600 hover:underline">terms and conditions</a></label>
                    </div>
                    <button type="submit" class="submit-button">Sign Up</button>
                </form>
                <div class="divider">or</div>
                <%
                    AppConfig appConfig = new AppConfig();
                %>
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=<%= appConfig.get("google.redirect_uri") %>&response_type=code&client_id=<%= appConfig.get("google.client_id") %>&approval_prompt=force"
                   class="social-button">
                    <svg class="w-5 h-5 text-red-600" aria-hidden="true" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z"/>
                    </svg>
                    Continue with Google
                </a>
                <a href="https://www.facebook.com/v20.0/dialog/oauth?client_id=1043400247735306&redirect_uri=http://localhost:8080/ReLoop/LoginServlet&state=facebook&scope=email"
                   class="social-button mt-2">
                    <svg class="w-5 h-5 text-blue-600" aria-hidden="true" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M16 8.049c0-4.446-3.582-8.05-8-8.05C3.58 0-.002 3.603-.002 8.05c0 4.017 2.926 7.347 6.75 7.951v-5.625h-2.03V8.05H6.75V6.275c0-2.017 1.195-3.131 3.022-3.131.876 0 1.791.157 1.791.157v1.98h-1.009c-.993 0-1.303.621-1.303 1.258v1.51h2.218l-.354 2.326H9.25V16c3.824-.604 6.75-3.934 6.75-7.951z"/>
                    </svg>
                    Continue with Facebook
                </a>
            </section>
        </div>
    </body>
</html>