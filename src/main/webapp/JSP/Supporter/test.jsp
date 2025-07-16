<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SecondHand - Supporter Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#4F46E5',
                        secondary: '#10B981',
                        danger: '#EF4444',
                        warning: '#F59E0B',
                        dark: '#1F2937',
                        light: '#F3F4F6',
                    }
                }
            }
        }
    </script>
    <style>
        .sidebar-item.active {
            background-color: rgba(79, 70, 229, 0.1);
            border-left: 4px solid #4F46E5;
            color: #4F46E5;
        }
        .sidebar-item:hover:not(.active) {
            background-color: rgba(79, 70, 229, 0.05);
        }
        .chat-message.sent {
            background-color: #4F46E5;
            color: white;
            border-radius: 1rem 1rem 0 1rem;
            align-self: flex-end;
        }
        .chat-message.received {
            background-color: #E5E7EB;
            color: #1F2937;
            border-radius: 1rem 1rem 1rem 0;
            align-self: flex-start;
        }
        .report-item.unresolved {
            border-left: 4px solid #EF4444;
        }
        .report-item.resolved {
            border-left: 4px solid #10B981;
        }
        .report-item.pending {
            border-left: 4px solid #F59E0B;
        }
        .unread-count {
            position: absolute;
            top: -0.5rem;
            right: -0.5rem;
            background-color: #EF4444;
            color: white;
            border-radius: 50%;
            width: 1.25rem;
            height: 1.25rem;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.75rem;
            font-weight: bold;
        }
    </style>
</head>
<body class="bg-gray-100 font-sans flex h-screen overflow-hidden">
    <!-- Sidebar -->
    <div class="w-64 bg-white shadow-md flex flex-col">
        <!-- Logo -->
        <div class="p-4 border-b border-gray-200 flex items-center justify-center">
            <div class="flex items-center space-x-2">
                <i class="fas fa-recycle text-2xl text-primary"></i>
                <span class="text-xl font-bold text-dark">SecondHand</span>
            </div>
        </div>
        
        <!-- Supporter Info -->
        <div class="p-4 border-b border-gray-200 flex items-center space-x-3">
            <div class="relative">
                <img src="https://randomuser.me/api/portraits/women/65.jpg" alt="Supporter" class="w-10 h-10 rounded-full">
                <span class="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-white"></span>
            </div>
            <div>
                <p class="font-medium">Sarah Johnson</p>
                <p class="text-xs text-gray-500">Supporter</p>
            </div>
        </div>
        
        <!-- Navigation -->
        <div class="flex-1 overflow-y-auto">
            <nav class="p-2">
                <div class="space-y-1">
                    <a href="#" class="sidebar-item active flex items-center px-4 py-3 text-sm font-medium rounded-md">
                        <i class="fas fa-tachometer-alt mr-3 text-primary"></i>
                        Dashboard
                    </a>
                    <a href="#" class="sidebar-item flex items-center px-4 py-3 text-sm font-medium rounded-md">
                        <i class="fas fa-flag mr-3 text-danger"></i>
                        Product Reports
                        <span class="ml-auto unread-count">5</span>
                    </a>
                    <a href="#" class="sidebar-item flex items-center px-4 py-3 text-sm font-medium rounded-md">
                        <i class="fas fa-comments mr-3 text-secondary"></i>
                        Messages
                        <span class="ml-auto unread-count">3</span>
                    </a>
                    <a href="#" class="sidebar-item flex items-center px-4 py-3 text-sm font-medium rounded-md">
                        <i class="fas fa-users mr-3 text-warning"></i>
                        User Management
                    </a>
                    <a href="#" class="sidebar-item flex items-center px-4 py-3 text-sm font-medium rounded-md">
                        <i class="fas fa-cog mr-3 text-gray-500"></i>
                        Settings
                    </a>
                </div>
            </nav>
        </div>
        
        <!-- Logout -->
        <div class="p-4 border-t border-gray-200">
            <a href="#" class="flex items-center px-4 py-2 text-sm font-medium text-gray-700 rounded-md hover:bg-gray-100">
                <i class="fas fa-sign-out-alt mr-3 text-gray-500"></i>
                Logout
            </a>
        </div>
    </div>
    
    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Top Navigation -->
        <header class="bg-white shadow-sm z-10">
            <div class="px-6 py-3 flex items-center justify-between">
                <h1 class="text-xl font-semibold text-gray-800">Supporter Dashboard</h1>
                <div class="flex items-center space-x-4">
                    <div class="relative">
                        <button class="p-1 rounded-full text-gray-500 hover:text-gray-700 hover:bg-gray-100">
                            <i class="fas fa-bell text-xl"></i>
                            <span class="unread-count">2</span>
                        </button>
                    </div>
                    <div class="relative">
                        <button class="flex items-center space-x-2">
                            <img src="https://randomuser.me/api/portraits/women/65.jpg" alt="User" class="w-8 h-8 rounded-full">
                        </button>
                    </div>
                </div>
            </div>
        </header>
        
        <!-- Dashboard Content -->
        <main class="flex-1 overflow-y-auto p-6">
            <!-- Stats Cards -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Pending Reports</p>
                            <p class="text-2xl font-bold text-warning mt-1">12</p>
                        </div>
                        <div class="p-3 rounded-full bg-yellow-100 text-warning">
                            <i class="fas fa-exclamation-triangle text-xl"></i>
                        </div>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Resolved Today</p>
                            <p class="text-2xl font-bold text-secondary mt-1">8</p>
                        </div>
                        <div class="p-3 rounded-full bg-green-100 text-secondary">
                            <i class="fas fa-check-circle text-xl"></i>
                        </div>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Active Chats</p>
                            <p class="text-2xl font-bold text-primary mt-1">5</p>
                        </div>
                        <div class="p-3 rounded-full bg-indigo-100 text-primary">
                            <i class="fas fa-comments text-xl"></i>
                        </div>
                    </div>
                </div>
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Banned Users</p>
                            <p class="text-2xl font-bold text-danger mt-1">3</p>
                        </div>
                        <div class="p-3 rounded-full bg-red-100 text-danger">
                            <i class="fas fa-user-slash text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Two Column Layout -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <!-- Reports Section -->
                <div class="lg:col-span-2">
                    <div class="bg-white rounded-lg shadow overflow-hidden">
                        <div class="px-6 py-4 border-b border-gray-200">
                            <div class="flex items-center justify-between">
                                <h2 class="text-lg font-semibold text-gray-800">Recent Product Reports</h2>
                                <div class="flex space-x-2">
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-primary text-white hover:bg-indigo-700">
                                        <i class="fas fa-filter mr-1"></i> Filter
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-gray-100 text-gray-700 hover:bg-gray-200">
                                        <i class="fas fa-sync-alt mr-1"></i> Refresh
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="divide-y divide-gray-200 overflow-y-auto" style="max-height: 500px;">
                            <!-- Report Item 1 -->
                            <div class="report-item unresolved px-6 py-4 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-start justify-between">
                                    <div class="flex-1 min-w-0">
                                        <div class="flex items-center space-x-2">
                                            <span class="text-sm font-medium text-danger">High Priority</span>
                                            <span class="text-xs text-gray-500">? 15 min ago</span>
                                        </div>
                                        <p class="mt-1 text-sm font-medium text-gray-900 truncate">Fake Rolex Watch</p>
                                        <p class="text-sm text-gray-500">Reported by: Michael Brown</p>
                                        <p class="mt-2 text-sm text-gray-600">User claims the watch is counterfeit and not as described in the listing.</p>
                                    </div>
                                    <div class="ml-4 flex-shrink-0">
                                        <img class="h-12 w-12 rounded-md object-cover" src="https://images.unsplash.com/photo-1523170335258-f5ed118494a8?ixlib=rb-1.2.1&auto=format&fit=crop&w=100&q=80" alt="Product">
                                    </div>
                                </div>
                                <div class="mt-3 flex space-x-2">
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-danger text-white hover:bg-red-700">
                                        <i class="fas fa-ban mr-1"></i> Ban User
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-secondary text-white hover:bg-green-700">
                                        <i class="fas fa-check mr-1"></i> Resolve
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-primary text-white hover:bg-indigo-700">
                                        <i class="fas fa-comment mr-1"></i> Message
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Report Item 2 -->
                            <div class="report-item pending px-6 py-4 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-start justify-between">
                                    <div class="flex-1 min-w-0">
                                        <div class="flex items-center space-x-2">
                                            <span class="text-sm font-medium text-warning">Medium Priority</span>
                                            <span class="text-xs text-gray-500">? 1 hour ago</span>
                                        </div>
                                        <p class="mt-1 text-sm font-medium text-gray-900 truncate">Used iPhone 12 - Not Working</p>
                                        <p class="text-sm text-gray-500">Reported by: Jessica Wilson</p>
                                        <p class="mt-2 text-sm text-gray-600">Buyer claims the phone doesn't turn on and was not disclosed in the description.</p>
                                    </div>
                                    <div class="ml-4 flex-shrink-0">
                                        <img class="h-12 w-12 rounded-md object-cover" src="https://images.unsplash.com/photo-1603921326210-6edd2d60ca68?ixlib=rb-1.2.1&auto=format&fit=crop&w=100&q=80" alt="Product">
                                    </div>
                                </div>
                                <div class="mt-3 flex space-x-2">
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-danger text-white hover:bg-red-700">
                                        <i class="fas fa-ban mr-1"></i> Ban User
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-secondary text-white hover:bg-green-700">
                                        <i class="fas fa-check mr-1"></i> Resolve
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-primary text-white hover:bg-indigo-700">
                                        <i class="fas fa-comment mr-1"></i> Message
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Report Item 3 -->
                            <div class="report-item resolved px-6 py-4 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-start justify-between">
                                    <div class="flex-1 min-w-0">
                                        <div class="flex items-center space-x-2">
                                            <span class="text-sm font-medium text-secondary">Resolved</span>
                                            <span class="text-xs text-gray-500">? 3 hours ago</span>
                                        </div>
                                        <p class="mt-1 text-sm font-medium text-gray-900 truncate">Stolen Bicycle</p>
                                        <p class="text-sm text-gray-500">Reported by: David Miller</p>
                                        <p class="mt-2 text-sm text-gray-600">User recognized the bike as their stolen property from police report.</p>
                                    </div>
                                    <div class="ml-4 flex-shrink-0">
                                        <img class="h-12 w-12 rounded-md object-cover" src="https://images.unsplash.com/photo-1511994298241-608e28f14fde?ixlib=rb-1.2.1&auto=format&fit=crop&w=100&q=80" alt="Product">
                                    </div>
                                </div>
                                <div class="mt-3 flex space-x-2">
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-gray-100 text-gray-700 hover:bg-gray-200">
                                        <i class="fas fa-eye mr-1"></i> View Details
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-primary text-white hover:bg-indigo-700">
                                        <i class="fas fa-comment mr-1"></i> Message
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Report Item 4 -->
                            <div class="report-item unresolved px-6 py-4 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-start justify-between">
                                    <div class="flex-1 min-w-0">
                                        <div class="flex items-center space-x-2">
                                            <span class="text-sm font-medium text-danger">High Priority</span>
                                            <span class="text-xs text-gray-500">? 5 hours ago</span>
                                        </div>
                                        <p class="mt-1 text-sm font-medium text-gray-900 truncate">Inappropriate Content</p>
                                        <p class="text-sm text-gray-500">Reported by: Admin System</p>
                                        <p class="mt-2 text-sm text-gray-600">Automatic flag for prohibited item (weapons) and inappropriate images.</p>
                                    </div>
                                    <div class="ml-4 flex-shrink-0">
                                        <img class="h-12 w-12 rounded-md object-cover" src="https://images.unsplash.com/photo-1584735428934-9f8e1d6db044?ixlib=rb-1.2.1&auto=format&fit=crop&w=100&q=80" alt="Product">
                                    </div>
                                </div>
                                <div class="mt-3 flex space-x-2">
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-danger text-white hover:bg-red-700">
                                        <i class="fas fa-ban mr-1"></i> Ban User
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-secondary text-white hover:bg-green-700">
                                        <i class="fas fa-check mr-1"></i> Resolve
                                    </button>
                                    <button class="px-3 py-1 text-xs font-medium rounded-md bg-primary text-white hover:bg-indigo-700">
                                        <i class="fas fa-comment mr-1"></i> Message Admin
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Messages Section -->
                <div class="lg:col-span-1">
                    <div class="bg-white rounded-lg shadow overflow-hidden h-full flex flex-col">
                        <div class="px-6 py-4 border-b border-gray-200">
                            <h2 class="text-lg font-semibold text-gray-800">Recent Messages</h2>
                        </div>
                        <div class="flex-1 overflow-y-auto">
                            <!-- Message Thread 1 -->
                            <div class="px-4 py-3 border-b border-gray-200 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-center space-x-3">
                                    <div class="relative">
                                        <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="User" class="w-10 h-10 rounded-full">
                                        <span class="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-white"></span>
                                    </div>
                                    <div class="flex-1 min-w-0">
                                        <p class="text-sm font-medium text-gray-900">Robert Chen</p>
                                        <p class="text-sm text-gray-500 truncate">Hi, I need help with a refund for an item that...</p>
                                    </div>
                                    <div class="text-xs text-gray-500">2:15 PM</div>
                                </div>
                            </div>
                            
                            <!-- Message Thread 2 -->
                            <div class="px-4 py-3 border-b border-gray-200 hover:bg-gray-50 cursor-pointer bg-gray-50">
                                <div class="flex items-center space-x-3">
                                    <div class="relative">
                                        <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="User" class="w-10 h-10 rounded-full">
                                        <span class="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-white"></span>
                                    </div>
                                    <div class="flex-1 min-w-0">
                                        <p class="text-sm font-medium text-gray-900">Lisa Park</p>
                                        <p class="text-sm text-gray-500 truncate">The seller hasn't shipped my order after 5 days...</p>
                                    </div>
                                    <div class="text-xs text-gray-500">11:30 AM</div>
                                </div>
                            </div>
                            
                            <!-- Message Thread 3 -->
                            <div class="px-4 py-3 border-b border-gray-200 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-center space-x-3">
                                    <div class="relative">
                                        <img src="https://randomuser.me/api/portraits/men/75.jpg" alt="User" class="w-10 h-10 rounded-full">
                                        <span class="absolute bottom-0 right-0 w-3 h-3 bg-gray-500 rounded-full border-2 border-white"></span>
                                    </div>
                                    <div class="flex-1 min-w-0">
                                        <p class="text-sm font-medium text-gray-900">Admin Team</p>
                                        <p class="text-sm text-gray-500 truncate">New policy update regarding prohibited items...</p>
                                    </div>
                                    <div class="text-xs text-gray-500">Yesterday</div>
                                </div>
                            </div>
                            
                            <!-- Message Thread 4 -->
                            <div class="px-4 py-3 border-b border-gray-200 hover:bg-gray-50 cursor-pointer">
                                <div class="flex items-center space-x-3">
                                    <div class="relative">
                                        <img src="https://randomuser.me/api/portraits/men/12.jpg" alt="User" class="w-10 h-10 rounded-full">
                                        <span class="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-white"></span>
                                    </div>
                                    <div class="flex-1 min-w-0">
                                        <p class="text-sm font-medium text-gray-900">James Wilson</p>
                                        <p class="text-sm text-gray-500 truncate">How do I report a suspicious listing?...</p>
                                    </div>
                                    <div class="text-xs text-gray-500">Yesterday</div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Active Chat (hidden by default, would show when a message thread is clicked) -->
                        <div class="hidden flex-col border-t border-gray-200">
                            <!-- Chat Header -->
                            <div class="px-4 py-3 border-b border-gray-200 flex items-center justify-between bg-gray-50">
                                <div class="flex items-center space-x-3">
                                    <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="User" class="w-8 h-8 rounded-full">
                                    <div>
                                        <p class="text-sm font-medium">Lisa Park</p>
                                        <p class="text-xs text-gray-500">Active now</p>
                                    </div>
                                </div>
                                <div class="flex space-x-2">
                                    <button class="p-1 text-gray-500 hover:text-gray-700">
                                        <i class="fas fa-phone"></i>
                                    </button>
                                    <button class="p-1 text-gray-500 hover:text-gray-700">
                                        <i class="fas fa-ellipsis-v"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Chat Messages -->
                            <div class="flex-1 p-4 overflow-y-auto bg-gray-100 space-y-3" style="max-height: 300px;">
                                <div class="chat-message received px-4 py-2 max-w-xs">
                                    <p>Hi, I'm having an issue with a seller who hasn't shipped my order after 5 days.</p>
                                    <p class="text-xs text-gray-300 mt-1">11:30 AM</p>
                                </div>
                                <div class="chat-message sent px-4 py-2 max-w-xs">
                                    <p>Hello Lisa, I'm sorry to hear that. Can you share the order ID so I can look into this for you?</p>
                                    <p class="text-xs text-indigo-200 mt-1">11:32 AM</p>
                                </div>
                                <div class="chat-message received px-4 py-2 max-w-xs">
                                    <p>Sure, it's #SH-789456. The seller isn't responding to my messages either.</p>
                                    <p class="text-xs text-gray-300 mt-1">11:33 AM</p>
                                </div>
                            </div>
                            
                            <!-- Message Input -->
                            <div class="p-3 border-t border-gray-200">
                                <div class="flex items-center space-x-2">
                                    <button class="p-2 text-gray-500 hover:text-gray-700">
                                        <i class="fas fa-paperclip"></i>
                                    </button>
                                    <input type="text" placeholder="Type a message..." class="flex-1 px-3 py-2 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent">
                                    <button class="p-2 text-primary hover:text-indigo-700">
                                        <i class="fas fa-paper-plane"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <script>
        // Simple interactivity for the dashboard
        document.addEventListener('DOMContentLoaded', function() {
            // Sidebar navigation
            const sidebarItems = document.querySelectorAll('.sidebar-item');
            sidebarItems.forEach(item => {
                item.addEventListener('click', function(e) {
                    e.preventDefault();
                    sidebarItems.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                    
                    // In a real app, this would load different content based on the clicked item
                });
            });
            
            // Report item click
            const reportItems = document.querySelectorAll('.report-item');
            reportItems.forEach(item => {
                item.addEventListener('click', function() {
                    // In a real app, this would open a detailed view of the report
                    console.log('Report clicked:', this.querySelector('p').textContent);
                });
            });
            
            // Message thread click
            const messageThreads = document.querySelectorAll('.border-b.hover\\:bg-gray-50');
            messageThreads.forEach(thread => {
                thread.addEventListener('click', function() {
                    // In a real app, this would open the chat with this user
                    console.log('Opening chat with:', this.querySelector('p').textContent);
                    
                    // Show the chat panel (hidden by default in this demo)
                    const chatPanel = document.querySelector('.hidden.flex-col');
                    chatPanel.classList.remove('hidden');
                });
            });
        });
    </script>
</body>
</html>