CREATE TABLE `prediction_requests` (
   `prediction_request_id`	bigint	NOT NULL auto_increment primary key,
   `device_token` varchar(255) NOT NULL,
   `request_id`	int	NOT NULL,
   `prediction_result`	varchar(5000)	NULL DEFAULT '',
   `status`	varchar(255)	NOT NULL DEFAULT 'on_progress',
   `created_at`	timestamp	NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `modified_at` timestamp	NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);