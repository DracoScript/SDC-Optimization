import matplotlib.pyplot as plt
import os
import sys
import numpy as np

# check if dir was provided

if (len(sys.argv) < 1):
    print 'missing parameter directory'
    sys.exit()

reverse = 0
if (len(sys.argv) > 2):
    reverse = sys.argv[2]



# List all files from input path

mypath = sys.argv[1]
onlyfiles = [f for f in os.listdir(mypath) if os.path.isfile(os.path.join(mypath, f))]
print 'Loading '+str(len(onlyfiles))+' files...'
print ''



# Create lists

sa_gen = []
sa_best = []
sa_sol = []
sa_timer = []
ga_gen = []
ga_best = []
ga_sol = []
ga_timer = []



# Read files

for file_name in onlyfiles:

    if (file_name.find('.stat') < 0):
        continue;

    # Read data
    data = open(mypath+file_name)

    gen = []
    best = []
    sol = "";
    timer = []

    for line in data:
        line = line.replace('\n', '')
        aux = line.split(' ')
        gen.append(aux[0])
        timer.append(aux[len(aux)-1])
        if (reverse):
            best.append(-aux[1])
        else:
            best.append(aux[1])
        sol = aux[2]

    # Store in Simulated Annealing lists
    if (file_name.find('_SA.stat') > 0):

        sa_gen.append(gen)
        sa_best.append(best)
        sa_sol.append(sol)
        sa_timer.append(timer)

    # Store in Genetic Algorithm lists
    elif (file_name.find('_GA.stat') > 0):

        ga_gen.append(gen)
        ga_best.append(best)
        ga_sol.append(sol)
        ga_timer.append(timer)



# Convert all lists to arrays
sa_best = np.asarray(sa_best, 'int')
sa_gen = np.asarray(sa_gen, 'int')
sa_timer = np.asarray(sa_timer, 'int')
ga_best = np.asarray(ga_best, 'int')
ga_gen = np.asarray(ga_gen, 'int')
ga_timer = np.asarray(ga_timer, 'int')

# Statistics
sa_mean = np.mean(sa_best[:,-1])
sa_std = np.std(sa_best[:,-1])
sa_min = np.min(sa_best[:,-1])
sa_max = np.max(sa_best[:,-1])
sa_fit = np.argmin(sa_best[:,-1])
sa_meant = np.mean(sa_timer[:,-1])
sa_stdt = np.std(sa_timer[:,-1])
sa_mint = np.min(sa_timer[:,-1])
sa_maxt = np.max(sa_timer[:,-1])
ga_mean = np.mean(ga_best[:,-1])
ga_std = np.std(ga_best[:,-1])
ga_min = np.min(ga_best[:,-1])
ga_max = np.max(ga_best[:,-1])
ga_fit = np.argmin(ga_best[:,-1])
ga_meant = np.mean(ga_timer[:,-1])
ga_stdt = np.std(ga_timer[:,-1])
ga_mint = np.min(ga_timer[:,-1])
ga_maxt = np.max(ga_timer[:,-1])



# PLOT Best Solution
plt.subplot(2,2,1)
for i in range(len(sa_best)):
    plt.plot(sa_gen[i], sa_best[i], '-', color='lightblue', alpha=0.2)
for i in range(len(ga_best)):
    plt.plot(ga_gen[i], ga_best[i], '-', color='indianred', alpha=0.2)
# Best Solution
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_best[sa_fit], '-', color='blue', alpha=0.75, linewidth=3)
plt.plot(ga_gen[ga_fit], ga_best[ga_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(ga_gen[ga_fit], ga_best[ga_fit], '-', color='red', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, sa_mean + ga_mean)
plt.xlabel('generation')
plt.ylabel('fitness of best solution (days)')
leg = plt.legend(['SA', 'GA'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)

# PLOT Best Solution Statistics
plt.subplot(2,2,2)
# Plot details
plt.ylim(0, sa_mean + ga_mean)
box = plt.boxplot([sa_best[:,-1], ga_best[:,-1]], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
box['boxes'][1].set_facecolor('indianred')
leg = plt.legend(['SA', 'GA'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')
leg.legendHandles[1].set_marker('s')
leg.legendHandles[1].set_color('indianred')


# PLOT Timer
plt.subplot(2,2,3)
for i in range(len(sa_timer)):
    plt.plot(sa_gen[i], sa_timer[i], '-', color='lightblue', alpha=0.2)
for i in range(len(ga_timer)):
    plt.plot(ga_gen[i], ga_timer[i], '-', color='indianred', alpha=0.2)
# Best Solution Timer
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa_gen[sa_fit], sa_timer[sa_fit], '-', color='blue', alpha=0.75, linewidth=3)
plt.plot(ga_gen[ga_fit], ga_timer[ga_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(ga_gen[ga_fit], ga_timer[ga_fit], '-', color='red', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, sa_meant + ga_meant)
plt.xlabel('generation')
plt.ylabel('time spent (ns)')
leg = plt.legend(['SA', 'GA'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)

# PLOT Timer Statistics
plt.subplot(2,2,4)
# Plot details
plt.ylim(0, sa_meant + ga_meant)
box = plt.boxplot([sa_timer[:,-1], ga_timer[:,-1]], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
box['boxes'][1].set_facecolor('indianred')
leg = plt.legend(['SA', 'GA'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')
leg.legendHandles[1].set_marker('s')
leg.legendHandles[1].set_color('indianred')

# Print results before showing plot
print ''
print '>>> RESULTS - Mission Duration (days) <<<'
print ''
print 'Simulated Annealing:'
print '    Best Solution      = '+str(sa_best[sa_fit][-1])+" "+sa_sol[sa_fit]
print '    Mean of Solutions  = '+str(sa_mean)+" ["+str(sa_min)+"-"+str(sa_max)+"]"
print '    Standard Deviation = '+str(sa_std)
print ''
print 'Genetic Algorithm:'
print '    Best Solution      = '+str(ga_best[ga_fit][-1])+" "+ga_sol[ga_fit]
print '    Mean of Solutions  = '+str(ga_mean)+" ["+str(ga_min)+"-"+str(ga_max)+"]"
print '    Standard Deviation = '+str(ga_std)
print ''
print ''
print '>>> RESULTS - Optimization Time (ns) <<<'
print ''
print 'Simulated Annealing:'
print '    Best Solution      = '+str(sa_timer[sa_fit][-1])
print '    Mean of Solutions  = '+str(sa_meant)+" ["+str(sa_mint)+"-"+str(sa_maxt)+"]"
print '    Standard Deviation = '+str(sa_stdt)
print ''
print 'Genetic Algorithm:'
print '    Best Solution      = '+str(ga_timer[ga_fit][-1])
print '    Mean of Solutions  = '+str(ga_meant)+" ["+str(ga_mint)+"-"+str(ga_maxt)+"]"
print '    Standard Deviation = '+str(ga_stdt)
print ''

# PLOT Save
plt.savefig(mypath[0:-1]+'_plot.png')

# PLOT Show
#plt.show()