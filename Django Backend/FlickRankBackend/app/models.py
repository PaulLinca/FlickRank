from django.db import models

# Create your models here.
class userTest(models.Model):
    name = models.CharField(max_length=10)
    userId = models.IntegerField()

    def __str__(self):
        return self.name